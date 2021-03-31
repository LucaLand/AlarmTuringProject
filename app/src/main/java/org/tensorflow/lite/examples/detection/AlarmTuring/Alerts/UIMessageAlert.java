package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;
import org.tensorflow.lite.examples.detection.AlarmTuringActivity;
import org.tensorflow.lite.examples.detection.R;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class UIMessageAlert extends ThreadAlert {

    int notificationID=0;

    /** IMPLEMENTING CLASS */


    /**
     * This function will be execute by a dedicated Thread
     * Remember to use Thread.sleep() decrease the execution Frequency
     */
    @Override
    public void alarmAlert() {
        while (isEngaged()) {
            createNotify();
            Logger.write("MSG");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void createNotify(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AlarmTuringActivity.getContext(), AlarmTuringActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.tfl2_logo_dark)
                .setContentTitle("ALERT!")
                .setContentText(getMessage())
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(NotificationCompat.PRIORITY_HIGH)//Important for heads-up notification
                .setGroup("Group");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AlarmTuringActivity.getContext());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationID, builder.build());
        notificationID++;
        Logger.write("NOTIFY!");
    }
}
