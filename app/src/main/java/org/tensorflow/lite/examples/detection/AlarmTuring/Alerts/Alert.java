package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

public interface Alert {
    public void alert();
    public void setAlertMessage(String alertMessage);
    public String getMessage();

    public void reset();
    public boolean isEngaged();
    public void setEngaged(boolean engaged);
}
