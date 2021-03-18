package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class CategoryFilterFactory {

    public static CategoryFilter createSimpleFilter(DetectionCategoryType categoryType){
        switch (categoryType){
            case ANIMAL:
                return new CategoryFilter("Animal", "Animal");
            case PERSON:
                return new CategoryFilter("Person", "Person");
            case VEHICLE:
                return new CategoryFilter("Vehicle", "Vehicle");
            default:
                return null;
        }
    }



    public static CategoryFilter createRecognitionFilter(DetectionCategoryType categoryType){
        switch (categoryType){
            case ANIMAL:
                return new CategoryFilter("dog,cat,horse,bear,bird,cow;", "Animal");
            case PERSON:
                return new CategoryFilter("person;", "Person");
            case VEHICLE:
                return new CategoryFilter("car,truck,bicycle,motorcycle;", "Vehicle");
            default:
                throw new EnumConstantNotPresentException(DetectionCategoryType.class, categoryType.toString());
        }
    }

    public static List<CategoryFilter> initializeRecognitionFilters(){
        List<CategoryFilter> filterList = new LinkedList<>();

        try{
            return CategoryFilterFactory.initializeRecognitionFiltersFromFile();
        }catch (FileNotFoundException e) {
            filterList.add(CategoryFilterFactory.createRecognitionFilter(DetectionCategoryType.PERSON));
            filterList.add(CategoryFilterFactory.createRecognitionFilter(DetectionCategoryType.ANIMAL));
            filterList.add(CategoryFilterFactory.createRecognitionFilter(DetectionCategoryType.VEHICLE));
        }

        return filterList;
    }


    protected static List<CategoryFilter> initializeRecognitionFiltersFromFile() throws FileNotFoundException {
        /** TODO implement file read for category selection ("Animal: dog,cat,horse,etc;\n) */
        throw new FileNotFoundException();
    }

}


