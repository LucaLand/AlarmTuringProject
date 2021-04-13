package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;


import org.tensorflow.lite.examples.detection.AlarmTuring.Utils.Logger;

public class MailAlert extends GeneralAlert {

    /** IMPLEMENTING CLASS */

    @Override
    public void alert() {
        Logger.write("MAIL!\n" + getMessage());
    }

}
