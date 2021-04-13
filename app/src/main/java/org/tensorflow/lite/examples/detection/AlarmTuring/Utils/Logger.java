package org.tensorflow.lite.examples.detection.AlarmTuring.Utils;

public class Logger {
    public static void write(String message){
        System.out.println("--ALARM-TURING-- log:  " + message);
    }

    public static void writeDebug(String message){
        System.err.println("--ALARM-TURING-- Debug:  " + message);
    }
}
