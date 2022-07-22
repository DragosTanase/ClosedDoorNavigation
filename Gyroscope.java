package com.example.testsenzorclass;


import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.os.Bundle;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;


public class Gyroscope extends Senzor{

    protected SensorManager mSensorManager;
    protected Sensor mGyroscope;
    boolean headerCSV = true;


    Gyroscope() {};


    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float xGyroscope = sensorEvent.values[0];
            float yGyroscope = sensorEvent.values[1];
            float zGyroscope = sensorEvent.values[2];

            String once = "X" + "," + "Y" + "," + "Z";
            String entry = "\n" + String.format("%.2f", xGyroscope) + "," + String.format("%.2f", yGyroscope) + "," + String.format("%.2f", zGyroscope);

            try {
                if(xGyroscope!=0.00 && yGyroscope!=0.00) {
                    writeCSV("/gyroscope.csv", "X" + ", " + "Y" + ", " + "Z", entry);
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