package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;


import org.tensorflow.lite.examples.detection.tflite.Detector;

import java.util.LinkedList;
import java.util.List;

public class DetectionsFilterer {

    static int id =0;


    public static List<Detector.Recognition> FilterRecognition (List<Detector.Recognition> detectionList, List<CategoryFilter> filters, ConfidenceFilter confFilter){
        List<Detector.Recognition> detections = new LinkedList<>();
        if(id>=500){id=0;}

        for(Detector.Recognition detection : detectionList){
            for(CategoryFilter f : filters){
                if(f.check(detection) && confFilter.check(detection))
                    detections.add(new Detector.Recognition(
                            ""+ id++,
                            f.getAlias(),
                            detection.getConfidence(),
                            detection.getLocation()
                    ));
            }
        }
        return detections;
    }












    /************************************************
     * DEPRECATED
     * ***********************************************/
    @Deprecated
    public static List<Detector.Recognition> FilterDetection(List<Detector.Recognition> l){

        List<Detector.Recognition> Detections = new LinkedList<>();
        if(id>=500){id=0;}

        String category;
        for (Detector.Recognition detection : l) {

            category = detection.getTitle();
            if(new CategoryFilter("car,truck,bicycle,motorcycle;").check(category)) {
                Detections.add(
                        new Detector.Recognition(
                                "" + id++,
                                DetectionCategoryType.VEHICLE.getCategory(),
                                detection.getConfidence(),
                                detection.getLocation()));
            }
            else if(new CategoryFilter("dog,cat,horse,bear,bird,cow;").check(category)){
                Detections.add(
                        new Detector.Recognition(
                                "" + id++,
                                DetectionCategoryType.ANIMAL.getCategory(),
                                detection.getConfidence(),
                                detection.getLocation()));
            }else if(new CategoryFilter("person;").check(category)){
                Detections.add(
                        new Detector.Recognition(
                                "" + id++,
                                DetectionCategoryType.PERSON.getCategory(),
                                detection.getConfidence(),
                                detection.getLocation()));
            }
        }

        return Detections;
    }

    @Deprecated
    public static List<Detector.Recognition> FilterDetection2(List<Detector.Recognition> l){

        List<Detector.Recognition> Detections = new LinkedList<>();
        if(id>=500){id=0;}

        String category;
        for (Detector.Recognition detection : l) {

            category = detection.getTitle();
            if(category.equals("car") || category.equals("truck") || category.equals("bicycle") || category.equals("motorcycle")) {
                Detections.add(
                        new Detector.Recognition(
                                "" + id++,
                                detection.getTitle(),
                                detection.getConfidence(),
                                detection.getLocation()));
            }
            else if(category.equals("dog") || category.equals("cat") || category.equals("horse") || category.equals("bear") || category.equals("bird") || category.equals("cow")){
                Detections.add(
                        new Detector.Recognition(
                                "" + id++,
                                "Animal",
                                detection.getConfidence(),
                                detection.getLocation()));
            }else if(new CategoryFilter("person;").check(category)){
                Detections.add(
                        new Detector.Recognition(
                                "" + id++,
                                "Person",
                                detection.getConfidence(),
                                detection.getLocation()));
            }
        }

        return Detections;
    }


}
