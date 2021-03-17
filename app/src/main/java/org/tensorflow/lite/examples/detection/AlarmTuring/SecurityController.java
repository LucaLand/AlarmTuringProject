package org.tensorflow.lite.examples.detection.AlarmTuring;



import android.text.format.Time;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.Alert;
import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.sql.Timestamp;
import java.util.List;

public class SecurityController {

    private final List<RelationCategoryToAlert> relationList;
    Timestamp timestamp[];

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
        Alert alert = AlertFactory.createAlert(rel.getAlertType());
        int num = 0;
        int time = rel.getTimeSeconds();
        for(Detector.Recognition detection : detectionList){
            if(CategoryFilterFactory.createSimpleFilter(rel.getDetectionCategory()).categoryCheck(detection.getTitle())) {
                num++;
                if (num >= rel.getMinObjectNumber()) {
                    if (timestamp[nRel].equals(null))
                        timestamp[nRel] = new Timestamp(System.currentTimeMillis());

                    if (rel.isDECREASE_TIME_BY_NUMBER())
                        time = time/num;

                    if (timestamp[nRel].compareTo(new Timestamp(System.currentTimeMillis())) > time)
                        alert.alert();

                }else{
                    timestamp[nRel] = null;
                }
            }else{
                timestamp[nRel] = null;
            }


        }
    }

}
