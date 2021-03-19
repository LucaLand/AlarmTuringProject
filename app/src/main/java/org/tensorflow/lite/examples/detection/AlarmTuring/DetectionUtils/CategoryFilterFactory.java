package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils.FileSupport;
import org.tensorflow.lite.examples.detection.AlarmTuring.FileUtils.Filename;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevelFactory;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;


@Filename(fileName = "CategoryFilter.txt")
public class CategoryFilterFactory {

    public static CategoryFilter createSimpleFilter(DetectionCategoryType categoryType){
        switch (categoryType){
            case ANIMAL:
                return new CategoryFilter("Animal", DetectionCategoryType.ANIMAL);
            case PERSON:
                return new CategoryFilter("Person", DetectionCategoryType.PERSON);
            case VEHICLE:
                return new CategoryFilter("Vehicle", DetectionCategoryType.VEHICLE);
            default:
                return null;
        }
    }

    public static CategoryFilter createDefaultRecognitionFilter(DetectionCategoryType categoryType){
        switch (categoryType){
            case ANIMAL:
                return new CategoryFilter("dog,cat,horse,bear,bird,cow;", DetectionCategoryType.ANIMAL);
            case PERSON:
                return new CategoryFilter("person;", DetectionCategoryType.PERSON);
            case VEHICLE:
                return new CategoryFilter("car,truck,bicycle,motorcycle;", DetectionCategoryType.VEHICLE);
            default:
                throw new EnumConstantNotPresentException(DetectionCategoryType.class, categoryType.toString());
        }
    }

    public static List<CategoryFilter> initializeRecognitionFilters(){
        List<CategoryFilter> filterList = new LinkedList<>();

        try{
            return CategoryFilterFactory.initializeRecognitionFiltersFromFile();
        }catch (FileNotFoundException e) {
            filterList.add(CategoryFilterFactory.createDefaultRecognitionFilter(DetectionCategoryType.PERSON));
            filterList.add(CategoryFilterFactory.createDefaultRecognitionFilter(DetectionCategoryType.ANIMAL));
            filterList.add(CategoryFilterFactory.createDefaultRecognitionFilter(DetectionCategoryType.VEHICLE));
        }

        return filterList;
    }

    /** {"category":"ANIMAL": "dog,cat,horse,etc"}\n) */
    protected static List<CategoryFilter> initializeRecognitionFiltersFromFile() throws FileNotFoundException {
        String fileName = SecurityLevelFactory.class.getAnnotation(Filename.class).fileName();
        List<CategoryFilter> categoryFilterList = new LinkedList<>();

        try {
            categoryFilterList = FileSupport.readCategoryFilters(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException("File:" + fileName + " not found");
        }

        return categoryFilterList;
    }

    public static CategoryFilter createCategroyFilterJson(JSONObject json){
        try {
            return new CategoryFilter(json.getString("classes"), DetectionCategoryType.valueOf(json.getString("category")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}


