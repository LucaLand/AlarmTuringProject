package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;

public class MailAlert implements Alert {

    /** IMPLEMENTING CLASS */

    @Override
    public void alert() {
        Logger.write("Mail");
    }

}
