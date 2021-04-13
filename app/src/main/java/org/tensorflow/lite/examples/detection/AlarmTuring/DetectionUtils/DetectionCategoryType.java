package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;

public enum DetectionCategoryType {
    PERSON("Person"), ANIMAL("Animal"), VEHICLE("Vehicle");
    private String category;
    DetectionCategoryType(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
}


/*
public static DetectionCategoryType valueOfLabel(String label) {
        for (DetectionCategoryType e : values()) {
            if (e.getCategory().equals(label)) {
                return e;
            }
        }
        return null;
    }
 */