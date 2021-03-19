package org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils;

import org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils.FileSupport;
import org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils.Filename;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

@Filename(fileName = "SecurityLevels.txt")
public class SecurityLevelFactory {

    public static SecurityLevel createSecurityLevel(int lv, String name, String relations){
        return new SecurityLevelCustom(lv, name, RelationFactory.createRelationList(relations));
    }



    private static List<SecurityLevel> importLevelsFromFile() throws FileNotFoundException {
        String fileName = SecurityLevelFactory.class.getAnnotation(Filename.class).fileName();
        List<SecurityLevel> levelList = new LinkedList<>();

        try{
            levelList = FileSupport.readSecurityLevels(fileName);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File:" + fileName + " not found");
        }


        return levelList;
    }

    public static List<SecurityLevel> createSecurityLevelList(){
        List<SecurityLevel> levelsList = new LinkedList<>();

        for(SecurityLevel level: SecurityLevelEnum.values()){
            levelsList.add(level);
        }

        try{
            levelsList.addAll(importLevelsFromFile());
        } catch (FileNotFoundException e) {
            System.out.println("["+ SecurityLevelFactory.class + "]:\t" + e.getMessage());
        }

        return levelsList;
    }

}
