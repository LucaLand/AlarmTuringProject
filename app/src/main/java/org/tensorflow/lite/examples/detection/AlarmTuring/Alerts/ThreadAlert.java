package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import org.tensorflow.lite.examples.detection.AlarmTuring.Utils.Logger;


// TODO. Si protrebbe usare la put sull'HANDLER come fatto in camera activity, probabilmente è preferibile

public abstract class ThreadAlert extends Thread implements Alert {

    private boolean engaged = false;
    private String alertMessage;


    public ThreadAlert() {
    }

    //TEST METHOD FOR UI ALERT MESSAGE
    public ThreadAlert(String alertMessage) {
        this.alertMessage = alertMessage;
    }
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }
    public String getMessage(){
        return alertMessage;
    }

    public void reset(){
        engaged=false;
    }
    public boolean isEngaged() {
        return engaged;
    }
    public void setEngaged(boolean engaged) {
        this.engaged = engaged;
    }

    //Alert running in thread
    @Override
    public void run() {
            alarmAlert();
    }

    //Alert function that run one time only
    public void alert(){
        if(!engaged) {
            engaged = true;
            Logger.writeDebug("THREAD Created || NThread:" + Thread.activeCount());
            this.start();
        }
        Logger.write(this.getClass().toString() + ": ENGAGED!");
    }

    /**
     * This function will be execute by a dedicated Thread
     * Remember to use Thread.sleep() decrease the execution Frequency
     */
    public abstract void alarmAlert();

}
