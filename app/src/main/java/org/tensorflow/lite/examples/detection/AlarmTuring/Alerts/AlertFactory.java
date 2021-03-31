package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

import java.util.LinkedList;
import java.util.List;

public class AlertFactory {


    public static Alert createAlert(AlertType type){
        switch (type){
            case MSG:
                return new UIMessageThreadAlert();
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
    public static List<Alert> createAlert(String s){
        List<Alert> alertList = new LinkedList<>();
        if(s.contains(AlertType.MSG.getType())){alertList.add(new UIMessageThreadAlert());}
        if(s.contains(AlertType.MAIL.getType())){alertList.add(new MailAlert());} //to implement MailAlert class
        if(s.contains(AlertType.SOUND.getType())){alertList.add(new SoundAlert());} //to implement SoundAlert class
        //if(s.contains(AlertTypes.BOT.getType())){alertList.add(new Alert());} //to create BotAlert class

        return alertList;
    }


}
