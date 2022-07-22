package com.example.testsenzorclass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Barometer extends Senzor{

    protected SensorManager sensorManager;
    protected Sensor pressureSensor;


    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float pressure = sensorEvent.values[0];


            String entry = "\n" + String.format("%.3f", pressure);

            try {
                writeCSV("/barometer.csv", "data", entry);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
}
