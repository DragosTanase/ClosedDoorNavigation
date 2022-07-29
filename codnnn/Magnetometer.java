package com.example.uisimplu;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.io.FileNotFoundException;


public class Magnetometer extends Senzor{
    protected SensorManager magSensorManager;
    protected Sensor magSensor;

    protected float azimuth ; // angular measurement in a spherical coordinate system
    protected float pitch ;
    protected float roll;
    protected double tesla ;

    Magnetometer() {};
    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            azimuth = Math.round(sensorEvent.values[0]); // angular measurement in a spherical coordinate system
            pitch = Math.round(sensorEvent.values[1]);
            roll = Math.round(sensorEvent.values[2]);
            tesla = Math.sqrt((azimuth * azimuth) + (pitch * pitch) + (roll * roll)); // magnetic field strength

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
