package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CategoryFilter implements Filter<Detector.Recognition> {
    private List<String> listCategories;
    private final DetectionCategoryType alias;  //Animal / Person / Vehicle


    public CategoryFilter(String classes, DetectionCategoryType category) {
        this.listCategories = createCategories(classes);
        this.alias = category;
    }

    private List<String> createCategories(String categories){
        List<String> categoriesList = new LinkedList<>();
        Scanner s = new Scanner(categories);
        s.useDelimiter(",|;");

        while(s.hasNext()){
            categoriesList.add(s.next());
        }

        return categoriesList;
    }

    public DetectionCategoryType getCategory() {
        return alias;
    }
    public String getAlias(){return alias.getCategory();}


    @Override
    public boolean check(Detector.Recognition input) {
        for(String category : listCategories){
            if(input.getTitle().equals(category))
                return true;
        }
        return false;
    }

    public boolean check(String input) {
        for(String category : listCategories){
            if(input.equals(category))
                return true;
        }
        return false;
    }

    @Deprecated
    public CategoryFilter (String classes){
        this.listCategories = createCategories(classes);
        this.alias = null;
    }


}
