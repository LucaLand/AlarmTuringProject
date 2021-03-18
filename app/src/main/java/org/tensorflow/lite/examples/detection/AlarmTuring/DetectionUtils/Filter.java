package org.tensorflow.lite.examples.detection.AlarmTuring.DetectionUtils;

public interface Filter<T> {
    boolean check(T input);
}
