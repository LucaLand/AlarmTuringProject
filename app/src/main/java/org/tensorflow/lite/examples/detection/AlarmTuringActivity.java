package org.tensorflow.lite.examples.detection;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.Alert;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilter;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.ConfidenceFilter;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionsFilterer;
import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityController;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityControllerFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;
import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmTuringActivity extends DetectorActivity implements View.OnClickListener  {

    //CONSTANTS
    // Minimum detection confidence to track a detection.
    private final float MINIMUM_CONFIDENCE_TF_OD_API = 0.6f;
    private final int STARTING_SECURITY_LEVEL = 1;
    private final boolean STARTING_CONTROLLER_ENABLED = false;
    private final List<SecurityLevel> securityLevelList = SecurityControllerFactory.getSecurityLevelList();
    private final int BLINK_PERIOD_TIME = 10; //BLINK every 0.5sec (10frames)

    //VARIABLES
    private int secLevel;
    private SecurityController securityController;
    private boolean engaged = false;
    private int timer;

    //UI - GRAPHICS References
    private ImageView plusImageView, minusImageView;
    private TextView TextViewLevelNum, levelNameTextView, alarmtextView, alertMessageTextView, enabledtextView;
    private FloatingActionButton onOffButton, resetButton;
    private ProgressBar detectionLevelProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secLevel= STARTING_SECURITY_LEVEL;
        securityController = SecurityControllerFactory.createSecurityController(STARTING_SECURITY_LEVEL, STARTING_CONTROLLER_ENABLED);


        //UI Text and buttons getting with findViewById
        TextViewLevelNum = findViewById(R.id.textLevel_Num);
        levelNameTextView = findViewById(R.id.textLevel_Name);
        plusImageView = findViewById(R.id.plus);
        minusImageView = findViewById(R.id.minus);
        onOffButton = findViewById(R.id.buttonOnOffAlarm);
        resetButton = findViewById(R.id.buttonReset);
        alarmtextView = findViewById(R.id.labelAlarm);
        alertMessageTextView = findViewById(R.id.TextAlertMessage);
        detectionLevelProgressBar = findViewById(R.id.detectionLevelProgerssBar);
        enabledtextView = findViewById(R.id.enabledDistabledtextView);


        //Adding onClickListener
        plusImageView.setOnClickListener(this);
        minusImageView.setOnClickListener(this);
        onOffButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        setSecurityLevel(secLevel);
        detectionLevelProgressBar.setMax(securityController.getMinTime());
    }

    /**
     * Function Executed in background Thread
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected List<Detector.Recognition> alarmTuringMainFunc(List<Detector.Recognition> detections){

        //  FILTERING THE CATEGORIES VISUALIZED
        /** Initializing filters for the category to detect*/
        List<CategoryFilter> categoryFilterList = CategoryFilterFactory.initializeRecognitionFilters();

        ConfidenceFilter confFilter = new ConfidenceFilter(MINIMUM_CONFIDENCE_TF_OD_API);

        /**Filtering all the detection by allowed Category and minimum confidence */
        List<Detector.Recognition> results = DetectionsFilterer.FilterRecognition(detections, categoryFilterList, confFilter);

        /** RUN SECURITY CONTROLLER */
        securityController.run(results);
        return results;
    }

    @Override
    protected void processImage() {
        super.processImage();
        checkAlerts();
        updateLevelProgressBarr();
    }

    private void updateLevelProgressBarr() {
        detectionLevelProgressBar.setProgress((int)securityController.getMaxDetectionLevel(), true);
    }

    private void checkAlerts() {

        String alertMessage = "";
        List<Alert> alertList = securityController.getAlertList();
        for(Alert alert : alertList){
            if(alert.isEngaged()) {
                engaged = true;
                alertMessage += alert.getMessage();
            }
        }
        alertMessageTextView.setText(alertMessage);
        alarmBlink();
    }

    private void alarmBlink(){
        timer++;
        if(engaged){
            if(alarmtextView.getVisibility()==View.VISIBLE && timer>BLINK_PERIOD_TIME){
                alarmtextView.setVisibility(View.INVISIBLE);
                timer = 0;
            }
            else if(alarmtextView.getVisibility()==View.INVISIBLE && timer>BLINK_PERIOD_TIME){
                alarmtextView.setVisibility(View.VISIBLE);
                timer = 0;
            }
        }else
            alarmtextView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId){
            case R.id.plus:
                handleClickPlusButton();
                break;
            case R.id.minus:
                handleClickMinusButton();
                break;
            case R.id.buttonOnOffAlarm:
                handleOnOffButton();
                break;
            case R.id.buttonReset:
                handleResetButton();
                break;
        }
    }

    private void handleResetButton() {
        securityController.resetAlerts();
        securityController.setActivated(true);
        securityController.powerButton();
        engaged = false;
        alarmtextView.setVisibility(View.INVISIBLE);
        detectionLevelProgressBar.setProgress(0);
        detectionLevelProgressBar.incrementProgressBy(-detectionLevelProgressBar.getProgress());
    }

    private void handleClickPlusButton(){
        if (secLevel >= securityLevelList.size()-1) return;
        secLevel++;
        setSecurityLevel(secLevel);
    }

    private void handleClickMinusButton(){
        if (secLevel == 0) {
            return;
        }
        secLevel--;
        setSecurityLevel(secLevel);
    }

    private void setSecurityLevel(int level){
        boolean enabled = securityController.isActivated();
        securityController = SecurityControllerFactory.createSecurityController(level, enabled);
        // Set name info of the level in the bottom layout panel
        levelNameTextView.setText(securityLevelList.get(level).getNomeLivello());
        //Set level num in the UI
        TextViewLevelNum.setText(String.valueOf(secLevel));
    }

    private void handleOnOffButton(){

        securityController.powerButton();
        if(securityController.isActivated()){
            enabledtextView.setText("ENABLED");
            enabledtextView.setTextColor(Color.GREEN);
        }else{
            enabledtextView.setText("DISABLED");
            enabledtextView.setTextColor(Color.RED);
        }
        Logger.write("ENABLED/DISABLED!");
    }

}
