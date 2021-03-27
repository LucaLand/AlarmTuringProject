package org.tensorflow.lite.examples.detection.AlarmTuring;



import android.os.Build;

import androidx.annotation.RequiresApi;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.Alert;
import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;
import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SecurityController {

    //Constant
    private final int RESET_TIMEOUT_SECONDS = 5;

    //Array variables
    List<LocalDateTime> timeStamp = new ArrayList<>();
    List<LocalDateTime> timeStampDecreasing = new ArrayList<>();
    List<Float> detectionLevel = new ArrayList<>();
    List<Alert> alertList = new ArrayList<>();

    //Attributes
    private boolean activated = false;
    private final List<RelationCategoryToAlert> relationList;

    public SecurityController(SecurityLevel securityLevel) {
        this.relationList = securityLevel.getRel();
        for(int i=0;i<relationList.size();i++){
            detectionLevel.add(i,0f);
            timeStamp.add(i,LocalDateTime.now());
            timeStampDecreasing.add(i,LocalDateTime.now());
            /* Creating the alert that handles the specific Alerting detection */
            alertList.add(AlertFactory.createAlert(relationList.get(i).getAlertType()));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public synchronized void run(List<Detector.Recognition> detectionList){
        if(!activated)
            return;

        int numRel=0;
        for(RelationCategoryToAlert relation : relationList){
            checker(detectionList, relation, numRel);
            numRel++;
        }
        Logger.write("|| DetectionLevel: " + getMaxDetectionLevel()); //use: getLv(0)
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private synchronized void checker(List<Detector.Recognition> detectionList, RelationCategoryToAlert rel, int numRel){


        int alertTime = rel.getTimeSeconds();
        boolean increaseLevelMultyple = rel.isDECREASE_TIME_BY_NUMBER();

        boolean isDetected = false;
        int num;

        if((num = checkDetection(detectionList, rel)) != -1) {
            detectionLevelIncrease(numRel, increaseLevelMultyple, num);
            isDetected = true;
        }

        if(checkDetectionLevelRelation(numRel, alertTime)) {
            Alert alert = alertList.get(numRel);
            alert.setAlertMessage(rel.toString() + "num= " + num);
            alert.alert();
        }

        if(getLv(numRel)!=0 && !isDetected){
            Logger.write("ENTERED");
            detectionLevelDecrease(numRel);
        }
        //DEBUG
        Logger.writeDebug("NumRel: "+numRel+"|| Cateory: " + rel.getDetectionCategory().getCategory() + "|| Num: "+num +" min: "+rel.getMinObjectNumber()+"|| IsDetected: "+isDetected + "||");

    }


    private int checkDetection(List<Detector.Recognition> detectionList ,RelationCategoryToAlert rel){
        int num = 0, minOccurrences = rel.getMinObjectNumber();
        DetectionCategoryType category = rel.getDetectionCategory();

        for(Detector.Recognition detection : detectionList) {
            if(checkCategory(detection, category)){
                num++;
                if(minOccurrencesCheck(num, minOccurrences)){
                    return num;
                }
            }
        }
        return -1;
    }

    private boolean checkCategory(Detector.Recognition detection, DetectionCategoryType categoryType ){
        return CategoryFilterFactory.createSimpleFilter(categoryType).check(detection);
    }

    private boolean minOccurrencesCheck(int occ, int minOcc){
        return occ >= minOcc;
    }


    /**TODO. Il time check
     * In modo tale che la detection di un tale oggetto incrementi un contatore(level of alert) che rappresenta
     * i secondi di detection dell'elemento, ogni secondo nel quale avviene almeno una detection, e questo si azzeri
     * in caso passi un tempo standard (COSTANT) tipo 10s*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private synchronized void detectionLevelIncrease(int numRel, boolean decreaseTime, int numOcc){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime detectionTime;
        int diff;
        timeStampDecreasing.add(numRel,now);

        if(getLv(numRel) == 0){
            timeStamp.add(numRel, now);
            if(!decreaseTime)
                lvAdd(numRel, 1);
            else
                lvAdd(numRel, numOcc);
            //Debug
            Logger.writeDebug("DIFF: "+ 0 +" || TIMEScorsa: "+ now.toString() + " || TIMENow: " + now.toString());
        }else {
            detectionTime = timeStamp.get(numRel);
            if ((diff = compareDate(detectionTime, now)) >= 1) {
                if (!decreaseTime)
                    lvAdd(numRel, 1);
                else
                    lvAdd(numRel, numOcc);
                timeStamp.add(numRel, now);
            }
            //Debug
            Logger.writeDebug("DIFF: "+diff+" || TIMEScorsa: "+ detectionTime.toString() + " || TIMENow: " + now.toString());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private synchronized void detectionLevelDecrease(int numRel) {
        LocalDateTime lastDetectionTime, timeDiff;
        LocalDateTime now = LocalDateTime.now();
        int diff, diffFromLast;

        lastDetectionTime = timeStamp.get(numRel);
        diffFromLast = compareDate(lastDetectionTime, now);
        timeDiff = timeStampDecreasing.get(numRel);
        diff = compareDate(timeDiff, now);

        if(diffFromLast > RESET_TIMEOUT_SECONDS) {
            lvSet(numRel, 0);
            //Debug
            Logger.writeDebug("detectionLevelDecrease - DIFF: "+diffFromLast+" || TIMEScorsa: "+ lastDetectionTime.toString() + " || TIMENow: " + now.toString());
            return;
        }else if(diff >= 1) {
            lvAdd(numRel, -0.4f);
            timeStampDecreasing.add(numRel, now);
            //Debug
            Logger.writeDebug("detectionLevelDecrease - DIFF: "+diff+" || TIMEScorsa: "+ timeDiff.toString() + " || TIMENow: " + now.toString());
        }

    }


    private boolean checkDetectionLevelRelation(int numRel, int alertDetectionLevel){
        if(getLv(numRel)<0) {
            lvSet(numRel, 0);
            return false;
        }

        if(getLv(numRel) >= alertDetectionLevel)
            return true;

        return false;
    }

    public float getMaxDetectionLevel(){
        float max = 0;
        for(Float i : detectionLevel){
            if(i > max)
                max = i;
        }
        return max;
    }

    private int compareDate(LocalDateTime timeFrom, LocalDateTime timeTo){

        LocalDateTime tempDateTime = LocalDateTime.from( timeFrom );

        long years = tempDateTime.until( timeTo, ChronoUnit.YEARS );
        tempDateTime = tempDateTime.plusYears( years );

        long months = tempDateTime.until( timeTo, ChronoUnit.MONTHS );
        tempDateTime = tempDateTime.plusMonths( months );

        long days = tempDateTime.until( timeTo, ChronoUnit.DAYS );
        tempDateTime = tempDateTime.plusDays( days );

        long hours = tempDateTime.until( timeTo, ChronoUnit.HOURS );
        tempDateTime = tempDateTime.plusHours( hours );

        long minutes = tempDateTime.until( timeTo, ChronoUnit.MINUTES );
        tempDateTime = tempDateTime.plusMinutes( minutes );

        long seconds = tempDateTime.until( timeTo, ChronoUnit.SECONDS );

        return (int)seconds;

    }

    private void lvAdd(int numRel, float value){
        detectionLevel.set(numRel, (getLv(numRel)+value));
    }

    private void lvSet(int numRel, float value){
        detectionLevel.set(numRel, value);
    }

    private float getLv(int numRel){
        return detectionLevel.get(numRel);
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void powerButton() {
        setActivated(!activated);
        if(!activated){
            reset();
        }
    }

    public void resetAlerts(){
        for(Alert alert : alertList) alert.reset();
    }

    private void reset(){
        for(int i=0;i<relationList.size();i++){
            detectionLevel.add(i,0f);
            timeStamp.add(i,LocalDateTime.now());
            timeStampDecreasing.add(i,LocalDateTime.now());
        }
    }

    public List<Alert> getAlertList() {
        return alertList;
    }
}
