package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;

public abstract class Alert extends Thread{

    private boolean engaged = false;


    @Override
    public void run() {
            alarmAlert();
    }



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
