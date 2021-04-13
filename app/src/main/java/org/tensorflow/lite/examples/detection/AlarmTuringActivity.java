package org.tensorflow.lite.examples.detection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;
import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmTuringActivity extends DetectorActivity implements View.OnClickListener  {

    //CONSTANTS
    private final float MINIMUM_CONFIDENCE_TF_OD_API = 0.6f;    // Minimum detection confidence to track a detection.
    private final int STARTING_SECURITY_LEVEL = 1;
    private final List<SecurityLevel> securityLevelList = SecurityControllerFactory.getSecurityLevelList();
    private final int BLINK_PERIOD_TIME = 10; //BLINK every (10frames)
    public static final String CHANNEL_ID = "5";

    //Static Context
    private static Context context;

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
    private LinearLayout relationInfoBox;

    //SOUNDS
    private MediaPlayer enabledSoundPlayer, simpleBeapSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Saving Context
        AlarmTuringActivity.context = getApplicationContext();

        //SOUNDS
        enabledSoundPlayer = MediaPlayer.create(AlarmTuringActivity.getContext(), R.raw.enabled_sound);
        simpleBeapSoundPlayer = MediaPlayer.create(AlarmTuringActivity.getContext(), R.raw.beap_sound);

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
        relationInfoBox = (LinearLayout) findViewById(R.id.relationBox);

        //Adding onClickListener
        plusImageView.setOnClickListener(this);
        minusImageView.setOnClickListener(this);
        onOffButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        //NOTIFY
        createNotificationChannel();

        //INITIALIZATION
        secLevel= STARTING_SECURITY_LEVEL;
        setSecurityLevel(secLevel);
        detectionLevelProgressBar.setMax(securityController.getMaxTimeAlert());
    }

    /**
     * Function Executed in background Thread cyclically in ProcessImage()-super
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected List<Detector.Recognition> alarmTuringMainFunc(List<Detector.Recognition> detections){
        /** Initializing filters for the category to detect*/
        List<CategoryFilter> categoryFilterList = CategoryFilterFactory.initializeRecognitionFilters();

        /**Filtering all the detection by allowed Category and minimum confidence */
        ConfidenceFilter confFilter = new ConfidenceFilter(MINIMUM_CONFIDENCE_TF_OD_API);
        List<Detector.Recognition> results = DetectionsFilterer.FilterRecognition(detections, categoryFilterList, confFilter);

        /** RUN SECURITY CONTROLLER */
        securityController.run(results);
        return results;
    }

    /** RUNS CYCLICALLY*/
    @Override
    protected void processImage() {
        super.processImage();
        checkAlerts();
        updateLevelProgressBarr();
    }

    private void updateLevelProgressBarr() {
        detectionLevelProgressBar.setProgress((int)securityController.getMaxDetectionLevelProportionally(), true);
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
        if(!simpleBeapSoundPlayer.isPlaying())simpleBeapSoundPlayer.start();
        securityController.resetAlerts();
        securityController.setActivated(true);
        handleOnOffButton();
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
        securityController = SecurityControllerFactory.createSecurityController(level, true);
        handleOnOffButton();
        // Set name info of the level in the bottom layout panel
        levelNameTextView.setText(securityLevelList.get(level).getNomeLivello());
        //Set level num in the UI
        TextViewLevelNum.setText(String.valueOf(secLevel));
        //Initialize Max level of the progress bar to te minimum level to Alert
        detectionLevelProgressBar.setMax(securityController.getMaxTimeAlert());
        //Diplay Level info in bottom sheet layout
        this.displayLevelInfo();
        Logger.write("SECURITY LEVEL SET TO: " + level);
    }

    private void handleOnOffButton(){
        securityController.powerButton();
        if(securityController.isActivated()){
            enabledtextView.setText("ENABLED");
            enabledtextView.setTextColor(Color.GREEN);
            if(!enabledSoundPlayer.isPlaying()) enabledSoundPlayer.start();
        }else{
            enabledtextView.setText("DISABLED");
            enabledtextView.setTextColor(Color.RED);
        }
        Logger.write("ENABLED/DISABLED!");
    }

    public static Context getContext() {
        return context;
    }

    private void createTextView(String info){
        TextView infoTextView = new TextView(this);
        TextView separatorTextView = new TextView(this);
        separatorTextView.setText("\n" + getResources().getString(R.string.separatorString));
        separatorTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        infoTextView.setPadding(0,3,3,3);
        RelativeLayout.LayoutParams paramsH = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        paramsH.setMargins(10, 10, 10, 5);
        paramsH.alignWithParent = true;
        infoTextView.setLayoutParams(paramsH);
        infoTextView.setGravity(Gravity.CENTER);
        infoTextView.setText(info);
        relationInfoBox.addView(separatorTextView);
        relationInfoBox.addView(infoTextView);
    }

    private void displayLevelInfo(){
        relationInfoBox.removeAllViews();
        for(RelationCategoryToAlert rel: securityLevelList.get(secLevel).getRel()){
            createTextView(rel.infoToString());
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ciao";
            String description = "Ciao2";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
