package org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilter;
import org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils.CategoryFilterFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.Utils.Logger;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationCategoryToAlert;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.RelationFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevelFactory;
import org.tensorflow.lite.examples.detection.AlarmTuring.AlarmTuringActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class FileSupport {

    public static final String dirName = "alarmturing";

    public static BufferedReader openFile(String fileName) throws FileNotFoundException {
        try{
            BufferedReader r = new BufferedReader(new InputStreamReader(AlarmTuringActivity.getContext().getAssets().open(fileName)));
            Logger.writeDebug("OPENING FILE: " + fileName);
            if (r==null)
                throw new FileNotFoundException("File:" + fileName + " not found!");
            return r;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            throw new FileNotFoundException("File:" + fileName + " not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<SecurityLevel> readSecurityLevels(String fileName) throws FileNotFoundException {
        List<SecurityLevel> levelList = new LinkedList<>();
        String line;
        JSONObject json;
        SecurityLevel s;

        BufferedReader r = openFile(fileName);
        try {
            while ((line = r.readLine()) != null){
                json = new JSONObject(line);
                s = SecurityLevelFactory.createSecurityLevel(json.getInt("lv"), json.getString("name"), json.getString("rel"));
                levelList.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                r.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return levelList;
    }

    /**FILE Category:
     * {"category":"ANIMAL", "classes":"dog,cat,horse,bear,bird,cow;"}.
     * {"category":"PERSON", "classes":"person"}.
     * {"category":"VEHICLE", "classes":"car,truck,bicycle,motorcycle;"}.*/

    public static List<CategoryFilter> readCategoryFilters (String fileName) throws FileNotFoundException {
        List<CategoryFilter> categoryFilters = new LinkedList<>();
        String line;
        JSONObject json;
        CategoryFilter filter;

        BufferedReader r = openFile(fileName);
        try {
            while ((line = r.readLine()) != null){
                json = new JSONObject(line);
                filter = CategoryFilterFactory.createCategroyFilterJson(json);
                categoryFilters.add(filter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categoryFilters;
    }

    public static String loadStringFromFile (String fileName) {
        final File file = loadFileFromStorage(dirName, fileName);

        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String chatId = r.readLine();
            return chatId;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void saveStringOnFile (String fileName, String str) {
        final File file = loadFileFromStorage(dirName, fileName);
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(str);
            myWriter.close();
            Logger.write("Successfully wrote to the file.");
        } catch (IOException e) {
            Logger.writeDebug("An error occurred.");
            e.printStackTrace();
        }
    }

    public static File loadFileFromStorage (String directoryName, String fileName){
        final String root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + directoryName;
        final File myDir = new File(root);
        return new File(myDir, fileName);
    }

}

/**
 * Rel1: ANIMAL,MSG,10(s),2(minimum),true/false;\n
 * RelJson: {"category":"ANIMAL/PERSON/VEHICLE", "alertType":"SOUND/MAIL/", "time":5 (secondi), "minOcc":1/2..., "decreaseTime":true/false}.\n
 * SecurityLevels: {"lv": 1, "name":"custom1", "rel":"ANIMAL,SOUND,8,2,false"}.\n
 * CategoryFilter: {"category":"ANIMAL", "classes":"dog,cat,horse,bear,bird,cow;"}.\n
 * */
