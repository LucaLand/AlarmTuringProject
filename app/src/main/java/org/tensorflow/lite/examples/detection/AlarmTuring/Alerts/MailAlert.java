package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;


import android.widget.EditText;

import org.tensorflow.lite.examples.detection.AlarmTuring.AlarmTuringActivity;
import org.tensorflow.lite.examples.detection.AlarmTuring.MailSupport.GMailSender;
import org.tensorflow.lite.examples.detection.AlarmTuring.Utils.Logger;

public class MailAlert extends GeneralAlert {

    @Override
    public void alert() {
        if(!isEngaged()) {
            setEngaged(true);
            invioMail();
            Logger.write("MAIL!\n" + getMessage());
        }
    }

    private void invioMail() {
        try {
            AlarmTuringActivity.sender.sendMail("ALARM ALERT!",
                    "ALERT :: " + getMessage(),
                    "ALARM-TURING",
                    AlarmTuringActivity.mailRecipients);
            Logger.write("EMAIL SENDED!");
        } catch (Exception e) {
            Logger.writeDebug("SendMail"+ e.getMessage());
        }
    }

}
