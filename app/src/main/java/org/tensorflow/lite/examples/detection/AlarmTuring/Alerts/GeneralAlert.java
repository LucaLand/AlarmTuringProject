package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

public abstract class GeneralAlert implements Alert{
    private boolean engaged = false;
    private String alertMessage = "";

    @Override
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    @Override
    public String getMessage() {
        return alertMessage;
    }

    @Override
    public void reset() {
        engaged = false;
    }

    @Override
    public boolean isEngaged() {
        return engaged;
    }

    @Override
    public void setEngaged(boolean engaged) {
        this.engaged = engaged;
    }

    public abstract void alert();
}
