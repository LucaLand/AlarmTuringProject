package org.tensorflow.lite.examples.detection.AlarmTuring;



import android.os.Build;

import androidx.annotation.RequiresApi;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.Alert;
import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevelEnum;
import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SecurityController {

    private final int RESET_TIMEOUT_SECONDS = 5;
    List<LocalDateTime> timeStamp = new ArrayList<>(50);
    float detectionLevel[] = {0};



    private final List<RelationCategoryToAlert> relationList;


    public static SecurityController createSecurityController(SecurityLevelEnum securityLevel) {
        return new SecurityController(securityLevel);
    }

    public SecurityController(SecurityLevelEnum securityLevel) {
        this.relationList = securityLevel.getRel();
        if(timeStamp.get(10)!= null);
            System.out.println("LUCA");
        for(int i=0; i<timeStamp.size(); i++){

        }
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
        Logger.write("detectionLevel" + getMaxDetectionLevel());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checker(List<Detector.Recognition> detectionList, RelationCategoryToAlert rel, int nRel){
        /** Creating the alert that handles the specific Alerting detection*/
        Alert alertType = AlertFactory.createAlert(rel.getAlertType());

        int num = 0, minOccurrences = rel.getMinObjectNumber();
        int time = rel.getTimeSeconds();
        boolean decreaseTime = rel.isDECREASE_TIME_BY_NUMBER();

        boolean isDetected = false;

        for(Detector.Recognition detection : detectionList) {
            isDetected = false;
            if(checkCategory(detection, rel.getDetectionCategory())){
                num++;
                if(minOccurencesCheck(num, minOccurrences)){
                    isDetected = true;
                    if(detectionTimeCheck(nRel,time, decreaseTime, num))
                        alertType.alert();
                }
            }

        }

        if(!isDetected){
            timerCheck(nRel,time);
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
    private boolean detectionTimeCheck(int numRel, int time, boolean decreaseTime, int numOcc){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime detectionTime;

        if(detectionLevel[numRel] == 0){
            detectionTime = LocalDateTime.now();
            timeStamp.set(numRel, detectionTime);
            if(!decreaseTime)
                detectionLevel[numRel]++;
            else
                detectionLevel[numRel] += numOcc;

            return false;
        }else
            detectionTime = timeStamp.get(numRel);


        int diff = detectionTime.compareTo(now);

        if(diff > 1) {
            if(!decreaseTime)
                detectionLevel[numRel]++;
            else
                detectionLevel[numRel] += numOcc;

            timeStamp.set(numRel, now);
        }

        if(detectionLevel[numRel] >= time)
            return true;

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void timerCheck(int numRel, int time) {
        LocalDateTime startTime;
        LocalDateTime now = LocalDateTime.now();

        if(detectionLevel[numRel] == 0)
            return;
        else{
            startTime = timeStamp.get(numRel);
            int diff = startTime.compareTo(now);

            if(diff > RESET_TIMEOUT_SECONDS)
                detectionLevel[numRel] = 0;
            else if(diff > 1)
                detectionLevel[numRel] += -0.4;
        }
    }

    public float getMaxDetectionLevel(){
        float max = 0;
        for(float i : detectionLevel){
            if(i>max)
                max = i;
        }
        return max;
    }
}
