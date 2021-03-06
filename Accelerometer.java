package com.example.uisimplu;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import org.w3c.dom.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Accelerometer extends Senzor{

    protected SensorManager mSensorManager;
    protected Sensor mAccelerator;
    protected  float xAccelerometer;
    protected float yAccelerometer;
    protected float zAccelerometer;
    Accelerometer(){};

    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            xAccelerometer = sensorEvent.values[0];
            yAccelerometer = sensorEvent.values[1];
            zAccelerometer = sensorEvent.values[2];
            String entry = "\n" + String.format("%.2f", xAccelerometer) + "," + String.format("%.2f", yAccelerometer) + "," + String.format("%.2f", zAccelerometer);

            try {
                if(xAccelerometer!=0.00 && yAccelerometer!=9.81) {
                    writeCSV("/accelerometer.csv", "X" + ", " + "Y" + ", " + "Z", entry);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };



}
