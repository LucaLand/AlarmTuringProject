package org.tensorflow.lite.examples.detection.AlarmTuring;



import android.os.Build;

import androidx.annotation.RequiresApi;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.Alert;
import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;
import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;



public class SecurityController {

    private final int RESET_TIMEOUT_SECONDS = 10;

    private final List<RelationCategoryToAlert> relationList;


    public static SecurityController createSecurityController(SecurityLevel securityLevel) {
        return new SecurityController(securityLevel);
    }

    public SecurityController(SecurityLevel securityLevel) {
        this.relationList = securityLevel.getRel();

    }


    public void run(List<Detector.Recognition> detectionList){
        /** La check deve fare tutto;
         * Deve richiamare le 3 diverse funzioni per la detection con il time (Person, Vehicle, Animal)
         */
        int numRel=0;
        for(RelationCategoryToAlert relation : relationList){
            checker(detectionList, relation, numRel);
            numRel++;
        }
    }


    private void checker(List<Detector.Recognition> detectionList, RelationCategoryToAlert rel, int nRel){
        /** Creating the alert that handles the specific Alerting detection*/
        Alert alertType = AlertFactory.createAlert(rel.getAlertType());

        int num = 0, minOccurrences = rel.getMinObjectNumber();
        int time = rel.getTimeSeconds();


        for(Detector.Recognition detection : detectionList) {
            if(checkCategory(detection, rel.getDetectionCategory())){
                num++;
                if(minOccurencesCheck(num, minOccurrences)){
                    if(timeCheck(nRel))
                        alertType.alert();
                }
            }

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

    private boolean timeCheck(int numRel){
        /**TODO. Il time check
         * In modo tale che la detection di un tale oggetto incrementi un contatore(level of alert) che rappresenta
         * i secondi di detection dell'elemento, ogni secondo nel quale avviene almeno una detection, e questo si azzeri
         * in caso passi un tempo standard (COSTANT) tipo 10s*/
        return true;
    }
}
