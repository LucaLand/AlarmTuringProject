package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

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
}
