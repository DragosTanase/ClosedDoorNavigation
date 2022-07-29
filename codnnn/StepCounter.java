package com.example.uisimplu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.io.FileNotFoundException;

public class StepCounter extends Senzor{
    protected SensorManager msensorManager;
    protected Sensor mStepCounter;
    protected float stepCount;


    protected SensorManager sensorManager;


    StepCounter(){};
    protected SensorEventListener sensorEventListener = new SensorEventListener(){

        @Override
        public void onSensorChanged( SensorEvent sensorEvent) {
            stepCount = sensorEvent.values[0];

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