package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;

import java.util.List;

public class SecurityLevelCustom implements SecurityLevel {

    /**     PARAMETERS      */
    private final int lv;
    private final String nomeLivello;
    private String descrizione = "";
    private List<RelationCategoryToAlert> rel;

    /**     CONSTRUCTOR      */
    public SecurityLevelCustom(int lv, String tipoLivello, List<RelationCategoryToAlert> rel, String desc) {
        this.lv = lv;
        this.nomeLivello = tipoLivello;
        this.descrizione = desc;
        this.rel = rel;
    }
    public SecurityLevelCustom(int lv, String tipoLivello, List<RelationCategoryToAlert> rel) {
        this.lv = lv;
        this.nomeLivello = tipoLivello;
        this.rel = rel;
    }


    /**    GETTER     Interface   */
    @Override
    public int getLevel() {return lv;}
    @Override
    public String getNomeLivello() {return nomeLivello;}
    @Override
    public String getDescrizione() {return descrizione;}
    @Override
    public List<RelationCategoryToAlert> getRel() {return rel;}
}
