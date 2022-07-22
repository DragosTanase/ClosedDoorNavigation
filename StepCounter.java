package com.example.testsenzorclass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.io.FileNotFoundException;

public class StepCounter extends Senzor{
    protected SensorManager msensorManager;
    protected Sensor mStepCounter;

    StepCounter(){};

    protected SensorEventListener sensorEventListener = new SensorEventListener(){


        @Override
        public void onSensorChanged( SensorEvent sensorEvent) {
            float stepCount = sensorEvent.values[0];

            String entry = "\n" + String.valueOf(stepCount);
            try {
                writeCSV("/stepcounter.csv", "steps", entry);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


}
