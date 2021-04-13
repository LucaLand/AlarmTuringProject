package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils.FileSupport;
import org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils.Filename;
import org.tensorflow.lite.examples.detection.AlarmTuring.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Filename(fileName = "SecurityLevels.txt")
public class SecurityLevelFactory {

    public static List<SecurityLevel> createSecurityLevelList(){
        List<SecurityLevel> levelsList = new ArrayList<>(), fileLevelsList;
        SecurityLevel[] levels = SecurityLevelEnum.values();

        for(SecurityLevel level: SecurityLevelEnum.values()){
            levels[level.getLevel()] = level;
        }
        levelsList.addAll(Arrays.asList(levels));

        try{
            if((fileLevelsList = importLevelsFromFile()) != null)
                levelsList.addAll(fileLevelsList);
        } catch (FileNotFoundException e) {
            Logger.writeDebug("["+ SecurityLevelFactory.class + "]:\t" + e.getMessage());
        }

        return levelsList;
    }

    private static List<SecurityLevel> importLevelsFromFile() throws FileNotFoundException {
        String fileName = SecurityLevelFactory.class.getAnnotation(Filename.class).fileName();
        List<SecurityLevel> levelList;

        try{
            levelList = FileSupport.readSecurityLevels(fileName);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File:" + fileName + " not found");
        }


        return levelList;
    }

    public static SecurityLevel createSecurityLevel(int lv, String name, String relations){
        return new SecurityLevelCustom(lv, name, RelationFactory.createRelationList(relations));
    }







}
