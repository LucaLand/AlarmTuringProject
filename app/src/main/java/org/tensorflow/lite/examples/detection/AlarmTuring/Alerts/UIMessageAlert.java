package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import android.widget.TextView;

import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;

public class UIMessageAlert extends Alert{


    /** IMPLEMENTING CLASS */


    /**
     * This function will be execute by a dedicated Thread
     * Remember to use Thread.sleep() decrease the execution Frequency
     */
    @Override
    public void alarmAlert() {
        Logger.write("MSG");
    }

}
