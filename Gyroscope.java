package com.example.uisimplu;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.io.FileNotFoundException;



public class Gyroscope extends Senzor{

    protected SensorManager mSensorManager;
    protected Sensor mGyroscope;
    boolean headerCSV = true;
    protected float xGyroscope ;
    protected float yGyroscope ;
    protected float zGyroscope ;

    Gyroscope() {};


    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            xGyroscope = sensorEvent.values[0];
            yGyroscope = sensorEvent.values[1];
            zGyroscope = sensorEvent.values[2];

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