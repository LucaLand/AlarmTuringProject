package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.tensorflow.lite.examples.detection.AlarmTuring.Utils.Logger;
import org.tensorflow.lite.examples.detection.AlarmTuring.AlarmTuringActivity;
import org.tensorflow.lite.examples.detection.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SoundAlert extends ThreadAlert {

final MediaPlayer alertSoundPlayer = MediaPlayer.create(AlarmTuringActivity.getContext(), R.raw.alarm_buzzer);
    /** IMPLEMENTING CLASS */


    /**
     * This function will be execute by a dedicated Thread
     * Remember to use Thread.sleep() decrease the execution Frequency
     */
    @Override
    public void alarmAlert() {
        while(isEngaged()) {
            Logger.write("------SOUND------");
            if(!alertSoundPlayer.isPlaying())
                alertSoundPlayer.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
