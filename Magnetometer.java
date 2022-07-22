package com.example.testsenzorclass;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;


import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Magnetometer extends Senzor{
    protected SensorManager magSensorManager;
    protected Sensor magSensor;

    Magnetometer() {};
    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            float azimuth = Math.round(sensorEvent.values[0]); // angular measurement in a spherical coordinate system
            float pitch = Math.round(sensorEvent.values[1]);
            float roll = Math.round(sensorEvent.values[2]);

            double tesla = Math.sqrt((azimuth * azimuth) + (pitch * pitch) + (roll * roll)); // magnetic field strength

            String entry = "\n" + String.format("%.3f", azimuth) + "," + String.format("%.3f", pitch) + "," + String.format("%.3f", roll) + ", " + String.format("%.3f", tesla);

            try {
                writeCSV("/magnetometer.csv", "azimuth" + ", " + "pitch" + ", " + "roll" + ", " + "tesla", entry);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
