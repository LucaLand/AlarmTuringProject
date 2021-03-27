package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import android.widget.TextView;

import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;

public class SoundAlert extends Alert {


    /** IMPLEMENTING CLASS */


    /**
     * This function will be execute by a dedicated Thread
     * Remember to use Thread.sleep() decrease the execution Frequency
     */
    @Override
    public void alarmAlert() {
        while(isEngaged()) {
            Logger.write("------SOUND------");

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
