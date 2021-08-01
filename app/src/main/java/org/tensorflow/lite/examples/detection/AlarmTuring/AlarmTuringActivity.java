package org.tensorflow.lite.examples.detection.AlarmTuring;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.Alert;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilter;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.ConfidenceFilter;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionsFilterer;
import org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils.FileSupport;
import org.tensorflow.lite.examples.detection.AlarmTuring.MailSupport.GMailSender;
import org.tensorflow.lite.examples.detection.AlarmTuring.TelegramBotUtils.TelegramBot;
import org.tensorflow.lite.examples.detection.AlarmTuring.Utils.Logger;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;
import org.tensorflow.lite.examples.detection.DetectorActivity;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.io.File;
import java.util.List;

//TODO. Bug fixes: read the bugtofix.txt file in the root dir

@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmTuringActivity extends DetectorActivity implements View.OnClickListener  {

    //CONSTANTS
    private final float MINIMUM_CONFIDENCE_TF_OD_API = 0.6f;    // Minimum detection confidence to track a detection.
    private final int STARTING_SECURITY_LEVEL = 0;

    private final int BLINK_PERIOD_TIME = 10; //BLINK every (10frames)
    public static final String CHANNEL_ID = "5";

    //Static Context
    private static Context context;

    private List<SecurityLevel> securityLevelList;
    private List<CategoryFilter> categoryFilterList;

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
    private LinearLayout relationInfoBox, botChatIDLinearLayout;
    private SwitchCompat telegramBotSwitch;
    private EditText chatIDEditText;
    private Button chatIDConfirmButton;
    private EditText mailRecipientsEditText;
    private Button mailRecipientsConfirmButton;

    //SOUNDS
    private MediaPlayer enabledSoundPlayer, simpleBeapSoundPlayer;

    //BOT
    private final String botToken = "Token";
    private final TelegramBot bot = new TelegramBot(botToken);
    private String chatId = "";
    boolean telegramBotActive = false;
    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    private final String chatIdFileName = "chatIdSave.txt";

    private int botTime = 0;
    private final int FRAMES_FOR_BOT_SEND = 1500;

    //MAIL
    public static GMailSender sender = new GMailSender("nicolacipolla69@gmail.com", "{Password}");
    public static String mailRecipients = "";
    private final String mailFileName = "mailSave.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Saving Context
        AlarmTuringActivity.context = getApplicationContext();

        //Loading all security levels
        securityLevelList = SecurityControllerFactory.getSecurityLevelList();

        //Initializing filters for the category to detect
        categoryFilterList = CategoryFilterFactory.initializeRecognitionFilters();

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
        botChatIDLinearLayout = (LinearLayout) findViewById(R.id.botChatID_linearLayout);
        telegramBotSwitch = findViewById(R.id.telegram_bot_switch);
        chatIDEditText = findViewById(R.id.editTextChatID);
        chatIDConfirmButton = findViewById(R.id.chatIDButton);
        mailRecipientsEditText = findViewById(R.id.email_edit_text);
        mailRecipientsConfirmButton = findViewById(R.id.mailRecipientsButton);


        //Adding onClickListener
        plusImageView.setOnClickListener(this);
        minusImageView.setOnClickListener(this);
        onOffButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        chatIDConfirmButton.setOnClickListener(this);
        telegramBotSwitch.setOnCheckedChangeListener(this);
        mailRecipientsConfirmButton.setOnClickListener(this);


        //NOTIFY
        createNotificationChannel();

        //BOT PROVE
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.setThreadPolicy(policy);
        }
        //bot.sendToTelegram(chatId, "AlarmTuringApp Started!");



        //INITIALIZATION
        secLevel= STARTING_SECURITY_LEVEL;
        setSecurityLevel(secLevel);
        detectionLevelProgressBar.setMax(securityController.getMaxTimeAlert());
        chatId = FileSupport.loadStringFromFile(chatIdFileName);
        chatIDEditText.setText(chatId);
        mailRecipientsEditText.setText(FileSupport.loadStringFromFile(mailFileName));
        setMailRecipients();

    }

    /**
     * Function Executed in background Thread cyclically in ProcessImage()-super
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected List<Detector.Recognition> alarmTuringMainFunc(List<Detector.Recognition> detections){

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
        botAlerts();
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
                alertMessage += alert.getMessage()+"\n";
            }
        }
        alertMessageTextView.setText(alertMessage);
        alarmBlink();
    }
    //BOT
    private void botAlerts(){
        if(engaged && telegramBotActive){
            if(botTime == 0) {
                sendBotDetectionPhoto();
                String alertMessage = alertMessageTextView.getText().toString();
                sendBotMessages("---- ALERT! DETECTION ----");
                sendBotMessages(alertMessage);
            }
            botTime++;
            if(botTime >= FRAMES_FOR_BOT_SEND){
                botTime = 0;
            }
        }
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
                //BOT MESSAGES
                    if(securityController.isActivated()) sendBotMessages("Alarm Enabled!");
                    else sendBotMessages("Alarm Disabled!");
                break;
            case R.id.buttonReset:
                handleResetButton();
                sendBotMessages("Alarm Reset!");
                break;
            case R.id.chatIDButton:
                setBotChatID(chatIDEditText.getText().toString());
                makeToastMessage("ChatID Confirmed!", Toast.LENGTH_SHORT);
                break;
            case R.id.mailRecipientsButton:
                confirmEmail();
                makeToastMessage("Email Recipients Confirmed!", Toast.LENGTH_SHORT);
                break;

        }
    }

    private void handleResetButton() {
        if(!simpleBeapSoundPlayer.isPlaying())simpleBeapSoundPlayer.start();
        securityController.resetAlerts();
        securityController.setActivated(true);
        categoryFilterList = CategoryFilterFactory.initializeRecognitionFilters();
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
        handleResetButton();
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

        //BOT MESSAGES - LEVEL CHANGED
        sendBotMessages("SecurityLevel set to: " + level +"-"+securityLevelList.get(level).getNomeLivello()+".");
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            telegramBotActive = true;
            botChatIDLinearLayout.setVisibility(View.VISIBLE);
            telegramBotSwitch.setText("ON");
        }else{
            sendBotMessages("AlarmTuringBot Deactivated!");
            telegramBotActive = false;
            botChatIDLinearLayout.setVisibility(View.GONE);
            telegramBotSwitch.setText("OFF");
        }
        botTime = 0;
    }

    private void setBotChatID(String id){
        if(id!=null || !id.equals("")) {
            chatId = id;
            sendBotMessages("AlarmTuringApp Connected!");
            FileSupport.saveStringOnFile(chatIdFileName, chatId);
            botTime = 0;
            //TEST SEND PHOTO
            //sendBotDetectionPhoto();
        }
    }

    private void sendBotMessages(String msg){
        if(telegramBotActive)
            bot.sendToTelegram(chatId, msg);
    }

    private void sendBotDetectionPhoto(){
        String fileName = "detectionPhoto.png";
        saveDetectionPhoto(fileName);
        bot.sendPhoto(chatId, FileSupport.loadFileFromStorage(FileSupport.dirName, fileName));
    }

    public void saveDetectionPhoto(String fileName){
        ImageUtils.saveBitmap(croppedBitmap, fileName);
    }

    //TEST MAIL
    private void confirmEmail() {
        setMailRecipients();
        try {
            sender.sendMail("AlarmTuringApp",
                    "Email successfully connected to Alarm-Turing!",
                    "ALARM-TURING",
                    mailRecipients);
            Logger.write("EMAIL SENT!");
        } catch (Exception e) {
            Logger.writeDebug("SendMail"+ e.getMessage());
        }
    }

    private void setMailRecipients(){
        mailRecipients = mailRecipientsEditText.getText().toString();
        FileSupport.saveStringOnFile(mailFileName, mailRecipients);
    }

    private void makeToastMessage(CharSequence msg, int duration){
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }
}
