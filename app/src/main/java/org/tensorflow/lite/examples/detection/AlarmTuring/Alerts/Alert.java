package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import android.widget.TextView;

import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;


// TODO. Si protrebbe usare la put sull'HANDLER come fatto in camera activity, probabilmente è preferibile

public abstract class Alert extends Thread{

    private boolean engaged = false;
    private String alertMessage;


    public Alert() {
    }

    //TEST METHOD FOR UI ALERT MESSAGE
    public Alert(String alertMessage) {
        this.alertMessage = alertMessage;
    }
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }
    public String getMessage(){
        return alertMessage + "\n";
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

    public void reset(){
        engaged=false;
    }

    public boolean isEngaged() {
        return engaged;
    }

    private void setEngaged(boolean engaged) {
        this.engaged = engaged;
    }

    /**
     * This function will be execute by a dedicated Thread
     * Remember to use Thread.sleep() decrease the execution Frequency
     */
    public abstract void alarmAlert();

}
