package org.tensorflow.lite.examples.detection.AlarmTuring;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RelationFactory {

    public static List<RelationCategoryToAlert> createRelationList(String relations){ //Stringa fatta "Animal,MSG,10(s),2(minimum),true/false;
                                                                                        //              Vehicle,MAIL,5,1,true; Person"
        List<RelationCategoryToAlert> relationList = new LinkedList<>();

        Scanner s = new Scanner(relations);
        DetectionCategoryType category;
        AlertType alert;
        int time;
        int minOccurcences;
        boolean decreaseTime;

        s.useDelimiter(",|;\\s|;");
        while(s.hasNext()){
            category = DetectionCategoryType.valueOf(s.next());
            alert = AlertType.valueOf(s.next());
            time = s.nextInt();
            minOccurcences = s.nextInt();
            decreaseTime = s.nextBoolean();
            relationList.add(new RelationCategoryToAlert(category,alert,time,minOccurcences,decreaseTime));
        }
        return relationList;
    }
}
