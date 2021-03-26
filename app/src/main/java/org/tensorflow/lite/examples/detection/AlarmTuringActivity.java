package org.tensorflow.lite.examples.detection;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilter;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.ConfidenceFilter;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionsFilterer;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityController;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityControllerFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevelFactory;
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

    //VARIABLES
    private int secLevel;
    private SecurityController securityController;

    //UI References
    private ImageView plusImageView, minusImageView;
    private TextView TextViewLevelNum, levelNameTextView;


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

        //Adding onClickListener
        plusImageView.setOnClickListener(this);
        minusImageView.setOnClickListener(this);
    }

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
        }
    }

    private void handleClickPlusButton(){
        if (secLevel >= 9) return;
        secLevel++;
        TextViewLevelNum.setText(String.valueOf(secLevel));
        changeSecurityLevel(secLevel);
    }

    private void handleClickMinusButton(){
        if (secLevel == 1) {
            return;
        }
        secLevel--;
        TextViewLevelNum.setText(String.valueOf(secLevel));
        changeSecurityLevel(secLevel);
    }

    private void changeSecurityLevel(int level){
        boolean enbaled = securityController.isActivated();
        securityController = SecurityControllerFactory.createSecurityController(level, enbaled);
        // Set name info of the level in the bottom layout panel
        levelNameTextView.setText(String.valueOf(securityLevelList.get(level).getLevel()));
    }

    private void handleOnOffButton(){
        //TODO. Set Message ENABLED/DISABLED
        securityController.powerButton();
    }

}
