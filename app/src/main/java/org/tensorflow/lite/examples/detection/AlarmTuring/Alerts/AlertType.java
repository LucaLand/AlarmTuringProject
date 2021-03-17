package org.tensorflow.lite.examples.detection.AlarmTuring.Alerts;

public enum AlertType {
    MSG("MSG"), MAIL("MAIL"), SOUND("SOUND"), BOT("BOT");
    private String type;

    AlertType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}

/*
public static AlertType valueOfLabel(String label) {
        for (AlertType e : values()) {
            if (e.getType().equals(label)) {
                return e;
            }
        }
        return null;
    }
 */