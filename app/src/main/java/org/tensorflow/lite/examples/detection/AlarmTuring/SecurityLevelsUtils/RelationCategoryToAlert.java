package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;

public class RelationCategoryToAlert {

    private final DetectionCategoryType detectionCategory;
    private final AlertType alertType;
    private int TimeSeconds = 5;
    private int minObjectNumber = 1;
    private boolean DECREASE_TIME_BY_NUMBER = false;


    public RelationCategoryToAlert(DetectionCategoryType detectionCategory, AlertType alertType, int timeSeconds, int minObjectNumber, boolean DECREASE_TIME_BY_NUMBER) {
        this.detectionCategory = detectionCategory;
        this.alertType = alertType;
        TimeSeconds = timeSeconds;
        this.minObjectNumber = minObjectNumber;
        this.DECREASE_TIME_BY_NUMBER = DECREASE_TIME_BY_NUMBER;
    }

    public RelationCategoryToAlert(DetectionCategoryType detectionCategory, AlertType alertType) {
        this.detectionCategory = detectionCategory;
        this.alertType = alertType;
    }

    public DetectionCategoryType getDetectionCategory() {
        return detectionCategory;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public int getTimeSeconds() {
        return TimeSeconds;
    }

    public int getMinObjectNumber() {
        return minObjectNumber;
    }

    public boolean isDECREASE_TIME_BY_NUMBER() {
        return DECREASE_TIME_BY_NUMBER;
    }
}
