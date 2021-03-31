package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import java.util.LinkedList;
import java.util.List;

public class AlertFactory {


    public static ThreadAlert createAlert(AlertType type){
        switch (type){
            case MSG:
                return new UIMessageAlert();
            case MAIL:
                return new MailAlert();
            case SOUND:
                return new SoundAlert();
            case BOT:
                return null; //BOT will be implemented
            default:
                return null;
        }
    }



    /** BROKEN */
    public static List<ThreadAlert> createAlert(String s){
        List<ThreadAlert> alertList = new LinkedList<>();
        if(s.contains(AlertType.MSG.getType())){alertList.add(new UIMessageAlert());}
        if(s.contains(AlertType.MAIL.getType())){alertList.add(new MailAlert());} //to implement MailAlert class
        if(s.contains(AlertType.SOUND.getType())){alertList.add(new SoundAlert());} //to implement SoundAlert class
        //if(s.contains(AlertTypes.BOT.getType())){alertList.add(new Alert());} //to create BotAlert class

        return alertList;
    }


}
