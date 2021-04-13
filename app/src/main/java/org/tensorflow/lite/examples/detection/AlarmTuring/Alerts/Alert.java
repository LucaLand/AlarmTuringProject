package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

public interface Alert {
    void alert();
    void setAlertMessage(String alertMessage);
    String getMessage();

    void reset();
    boolean isEngaged();
    void setEngaged(boolean engaged);
}
