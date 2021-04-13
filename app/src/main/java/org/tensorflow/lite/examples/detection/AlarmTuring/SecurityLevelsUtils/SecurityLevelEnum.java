package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;

import java.util.Arrays;
import java.util.List;

public enum SecurityLevelEnum implements SecurityLevel {
    MAXSEC(6,"Sicurezza: Massima", "ANIMAL,MAIL,4,2,true; PERSON,SOUND,3,1,true; VEHICLE,SOUND,3,1,true;"),
    MEDIUMSEC(5,"Sicurezza: Media", "PERSON,SOUND,6,1,true; VEHICLE,SOUND,3,1,false;"),
    NIGHTSEC(4,"Sicurezza: Notturna", "ANIMAL,MSG,6,2,false; PERSON,SOUND,5,1,true; VEHICLE,MAIL,4,1,false;"),
    LOWSEC(3,"Sicurezza: Bassa", "ANIMAL,MSG,6,1,true; PERSON,MAIL,5,2,false;"),
    VEHICLE(2,"Veicoli", "VEHICLE,MSG,4,1,false"),
    PERSON(1,"Persone","PERSON,MSG,4,2,false;"),
    ANIMALS(0, "Animali", "ANIMAL,MSG,4,1,false; ANIMAL,SOUND,2,7,false;", "Descrizione opzionale");


    /**     PARAMETERS      */
    private final int lv;
    private final String nomeLivello;
    private List<RelationCategoryToAlert> rel;
    private String descrizione = "";


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
