package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;

import java.util.Arrays;
import java.util.List;

public enum SecurityLevelEnum implements SecurityLevel {
    MAXIMUM(6,"Sicurezza: Massima", "PERSON,SOUND,5,1,true; ANIMAL,SOUND,8,2,false; VEHICLE,SOUND,3,1,true"),
    FUORICASA(5,"Sicurezza:Alto Livello - Fuori casa","PERSON,SOUND,4,1,true; ANIMAL,MAIL,6,2,false; VEHICLE,MAIL,4,2,false;"),
    HIGH(4,"Sicurezza:Alto Livello - Fuori casa", "PERSON,SOUND,5,1,true; ANIMAL,MAIL,6,2,false; VEHICLE,MSG,5,1,false;"),
    MEDIUM(3,"Sicurezza: media", "PERSON,SOUND,8,1,true; ANIMAL,MAIL,8,2,false;"),
    INCASA(2,"Sicurezza: bassa - in casa", "PERSON,SOUND,8,2,true;"),
    LOW(1, "Sicurezza bassa", "PERSON,MSG,8,1,false;", "Descrizione");


    /**     PARAMETERS      */
    private final int lv;
    private final String nomeLivello;
    private String descrizione = "";
    private List<RelationCategoryToAlert> rel;

    /**     CONSTRUCTOR      */
    SecurityLevelEnum(int lv, String levelName, String relationString) {
        this.lv = lv;
        this.nomeLivello = levelName;
        this.rel = RelationFactory.createRelationList(relationString);
    }

    SecurityLevelEnum(int lv, String levelName, String relationString, String desc) {
        this.lv = lv;
        this.nomeLivello = levelName;
        this.rel = RelationFactory.createRelationList(relationString);
        this.descrizione = desc;
    }

    @Deprecated
    SecurityLevelEnum(int lv, String levelName, List<RelationCategoryToAlert> rel, String desc) {
        this.lv = lv;
        this.nomeLivello = levelName;
        this.descrizione = desc;
        this.rel = rel;
    }
    @Deprecated
    SecurityLevelEnum(int lv, String levelName, List<RelationCategoryToAlert> rel) {
        this.lv = lv;
        this.nomeLivello = levelName;
        this.rel = rel;
    }

    /**    GETTER     Interface   */
    public int getLevel() {return lv;}
    public String getNomeLivello() {return nomeLivello;}
    public String getDescrizione() {return descrizione;}
    public List<RelationCategoryToAlert> getRel() {return rel;}

}
