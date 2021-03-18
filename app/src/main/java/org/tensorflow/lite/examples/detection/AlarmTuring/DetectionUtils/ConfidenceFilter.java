package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

import org.tensorflow.lite.examples.detection.tflite.Detector;

public class ConfidenceFilter implements Filter<Detector.Recognition>{

    private float minConfidence;

    public ConfidenceFilter(float minConfidence) {
        this.minConfidence = minConfidence;
    }

    @Override
    public boolean check(Detector.Recognition input) {

        if(input.getConfidence() >= minConfidence)
            return true;
        return false;
    }
}
