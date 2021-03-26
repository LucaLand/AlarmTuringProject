package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;

public class SoundAlert implements Alert {

    /** IMPLEMENTING CLASS */

    @Override
    public void alert() {
        Logger.write("------SOUND------");
    }

}
