package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.Alerts.AlertType;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.DetectionCategoryType;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;

import java.util.Arrays;
import java.util.List;

public enum SecurityLevelEnum implements SecurityLevel {
    MAXIMUM(6,"Sicurezza: Massima",null), //IMPLEMENT desc e RELATION
    FUORICASA(5,"Sicurezza:Alto Livello - Fuori casa",null), //IMPLEMENT desc e RELATION
    HIGH(4,"Sicurezza:Alto Livello - Fuori casa",null), //IMPLEMENT desc e RELATION
    MEDIUM(3,"Sicurezza: media", null), //IMPLEMENT desc e RELATION
    INCASA(2,"Sicurezza: bassa - in casa", RelationFactory.createRelationList("PERSON,SOUND,8,1,true;")),
    LOW(1, "Sicurezza bassa", Arrays.asList(new RelationCategoryToAlert(DetectionCategoryType.PERSON,AlertType.MSG)), "Descrizione");


    /**     PARAMETERS      */
    private final int lv;
    private final String nomeLivello;
    private String descrizione = "";
    private List<RelationCategoryToAlert> rel;

    /**     CONSTRUCTOR      */
    SecurityLevelEnum(int lv, String tipoLivello, List<RelationCategoryToAlert> rel, String desc) {
        this.lv = lv;
        this.nomeLivello = tipoLivello;
        this.descrizione = desc;
        this.rel = rel;
    }
    SecurityLevelEnum(int lv, String tipoLivello, List<RelationCategoryToAlert> rel) {
        this.lv = lv;
        this.nomeLivello = tipoLivello;
        this.rel = rel;
    }

    /**    GETTER     Interface   */
    public int getLevel() {return lv;}
    public String getNomeLivello() {return nomeLivello;}
    public String getDescrizione() {return descrizione;}
    public List<RelationCategoryToAlert> getRel() {return rel;}















    /**         DEPRECATI           */
    @Deprecated
    private String alertTypes = AlertType.MSG.getType();
    @Deprecated
    private List<String> detectingCategory;
    @Deprecated
    SecurityLevelEnum(int lv, String tipoLivello, String desc, String alertTypes, List<String> categories) {
        this.lv = lv;
        this.nomeLivello = tipoLivello;
        this.descrizione = desc;
        this.alertTypes = alertTypes;
        this.detectingCategory = categories;

    }
    @Deprecated
    SecurityLevelEnum(int lv, String tipoLivello, String desc, String alertTypes) {
        this.lv = lv;
        this.nomeLivello = tipoLivello;
        this.descrizione = desc;
        this.alertTypes = alertTypes;
    }




}
