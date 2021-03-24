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

    private final int RESET_TIMEOUT_SECONDS = 5;
    List<LocalDateTime> timeStamp = new ArrayList<>(50);
    List<LocalDateTime> timeStampDecreasing = new ArrayList<>(50);
    float detectionLevel[] = {0};



    private final List<RelationCategoryToAlert> relationList;


    public static SecurityController createSecurityController(SecurityLevel securityLevel) {
        return new SecurityController(securityLevel);
    }

    public SecurityController(SecurityLevel securityLevel) {
        this.relationList = securityLevel.getRel();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run(List<Detector.Recognition> detectionList){
        /** La check deve fare tutto;
         *
         */
        int numRel=0;
        for(RelationCategoryToAlert relation : relationList){
            checker(detectionList, relation, numRel);
            numRel++;
        }
        Logger.write("|| DetectionLevel: " + detectionLevel[0]); //use: getMaxDetectionLevel()
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private synchronized void checker(List<Detector.Recognition> detectionList, RelationCategoryToAlert rel, int nRel){
        /** Creating the alert that handles the specific Alerting detection*/
        Alert alertType = AlertFactory.createAlert(rel.getAlertType());

        int num = 0, minOccurrences = rel.getMinObjectNumber();
        int alertTime = rel.getTimeSeconds();
        boolean decreaseTime = rel.isDECREASE_TIME_BY_NUMBER();
        DetectionCategoryType category = rel.getDetectionCategory();
        boolean isDetected = false;

        for(Detector.Recognition detection : detectionList) {
            isDetected = false;
            if(checkCategory(detection, category)){
                num++;
                if(minOccurencesCheck(num, minOccurrences)){
                    isDetected = true;
                    detectionLevelIncrease(nRel, decreaseTime, num);
                }
                //DEBUG
                Logger.writeDebug("NumRel: "+nRel+"|| Cateory: " + category.getCategory() + "|| Num: "+num +" min: "+minOccurrences+"|| IsDetected: "+isDetected + "||");

            }
        }

        if(checkDetectionLevelofRelation(nRel, alertTime))
            alertType.alert();

        if(detectionLevel[nRel]!=0 && !isDetected){
            Logger.write("ENTERED");
            detectionLevelDecrease(nRel);
        }

    }



    private boolean checkCategory(Detector.Recognition detection, DetectionCategoryType categoryType ){
        return CategoryFilterFactory.createSimpleFilter(categoryType).check(detection);
    }

    private boolean minOccurencesCheck(int occ, int minOcc){
        if(occ >= minOcc)
            return true;
        return false;
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

        if(detectionLevel[numRel] == 0){
            timeStamp.add(numRel, now);
            if(!decreaseTime)
                detectionLevel[numRel]++;
            else
                detectionLevel[numRel] += numOcc;
            //Debug
            Logger.writeDebug("DIFF: "+ 0 +" || TIMEScorsa: "+ now.toString() + " || TIMENow: " + now.toString());
        }else {
            detectionTime = timeStamp.get(numRel);
            if ((diff = compareDate(detectionTime, now)) >= 1) {
                if (!decreaseTime)
                    detectionLevel[numRel]++;
                else
                    detectionLevel[numRel] += numOcc;
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
            detectionLevel[numRel] = 0;
            //Debug
            Logger.writeDebug("detectionLevelDecrease - DIFF: "+diffFromLast+" || TIMEScorsa: "+ lastDetectionTime.toString() + " || TIMENow: " + now.toString());
            return;
        }else if(diff >= 1) {
            detectionLevel[numRel] += -0.4;
            timeStampDecreasing.add(numRel, now);
            //Debug
            Logger.writeDebug("detectionLevelDecrease - DIFF: "+diff+" || TIMEScorsa: "+ timeDiff.toString() + " || TIMENow: " + now.toString());
        }

    }


    private boolean checkDetectionLevelofRelation(int numRel, int alertDetectionLevel){
        if(detectionLevel[numRel]<0) {
            detectionLevel[numRel] = 0;
            return false;
        }
        if(detectionLevel[numRel] >= alertDetectionLevel)
            return true;

        return false;
    }

    public float getMaxDetectionLevel(){
        float max = 0;
        for(float i : detectionLevel){
            if(i>max)
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
}
