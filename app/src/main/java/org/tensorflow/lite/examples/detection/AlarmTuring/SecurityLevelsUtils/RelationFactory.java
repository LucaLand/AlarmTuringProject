package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RelationFactory {

    public static List<RelationCategoryToAlert> createRelationList(String relations){ //Stringa fatta "ANIMAL,MSG,10(s),2(minimum),true/false;
                                                                                        //              VEHICLE,MAIL,5,1,true; PERSON"
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

    public static RelationCategoryToAlert createRelation(JSONObject json){
        try {
            DetectionCategoryType category = DetectionCategoryType.valueOf(json.getString("category"));
            AlertType alertType = AlertType.valueOf(json.getString("alertType"));
            int time = json.getInt("time");
            int minOcc = json.getInt("minOcc");
            boolean decreaseTime = json.getBoolean("decreaseTime");
            return new RelationCategoryToAlert(category, alertType, time, minOcc, decreaseTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
