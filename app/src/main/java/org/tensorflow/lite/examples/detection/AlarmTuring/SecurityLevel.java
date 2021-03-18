package org.tensorflow.lite.examples.detection.AlarmTuring;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum SecurityLevel {
    MAXIMUM(6,"Sicurezza: Massima",""), //IMPLEMENT desc e RELATION
    FUORICASA(5,"Sicurezza:Alto Livello - Fuori casa",""), //IMPLEMENT desc e RELATION
    HIGH(4,"Sicurezza:Alto Livello - Fuori casa",""), //IMPLEMENT desc e RELATION
    MEDIUM(3,"Sicurezza: media", ""), //IMPLEMENT desc e RELATION

    INCASA(2,"Sicurezza: bassa - in casa", "", RelationFactory.createRelationList("PERSON,SOUND,8,1,true;")),
    LOW(1, "Sicurezza bassa", "", Arrays.asList(new RelationCategoryToAlert(DetectionCategoryType.PERSON,AlertType.MSG)));


    private final int lv;
    private final String tipoLivello;
    private String descrizione = "";
    private List<RelationCategoryToAlert> rel;

    public static SecurityLevel createLevelsFromFile(){
        /** TODO implement factory method createLevelsFromFile to import personalized SecurityLevels */
        return null;
    }


    SecurityLevel(int lv, String tipoLivello, String desc, List<RelationCategoryToAlert> rel) {
        this.lv = lv;
        this.tipoLivello = tipoLivello;
        this.descrizione = descrizione;
        this.rel = rel;
    }
    SecurityLevel(int lv, String tipoLivello, String desc) {
        this.lv = lv;
        this.tipoLivello = tipoLivello;
        this.descrizione = desc;
    }

    SecurityLevel(int lv, String tipoLivello) {
        this.lv = lv;
        this.tipoLivello = tipoLivello;
    }

    public int getLevel() {
        return lv;
    }

    public String getTipoLivello() {
        return tipoLivello;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<RelationCategoryToAlert> getRel() {
        return rel;
    }

    @Deprecated
    private String alertTypes = AlertType.MSG.getType();
    @Deprecated
    private List<String> detectingCategory;
    @Deprecated
    SecurityLevel(int lv, String tipoLivello, String desc, String alertTypes, List<String> categories) {
        this.lv = lv;
        this.tipoLivello = tipoLivello;
        this.descrizione = desc;
        this.alertTypes = alertTypes;
        this.detectingCategory = categories;

    }
    @Deprecated
    SecurityLevel(int lv, String tipoLivello, String desc, String alertTypes) {
        this.lv = lv;
        this.tipoLivello = tipoLivello;
        this.descrizione = desc;
        this.alertTypes = alertTypes;
    }


}
