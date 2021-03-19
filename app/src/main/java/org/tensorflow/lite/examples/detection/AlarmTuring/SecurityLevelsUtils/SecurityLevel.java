package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;

import java.util.List;

public interface SecurityLevel {

    /**     GETTER          */
    public int getLevel();
    public String getNomeLivello();
    public String getDescrizione();
    public List<RelationCategoryToAlert> getRel();
}
