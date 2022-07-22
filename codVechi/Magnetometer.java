package com.example.mainapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class Magnetometer {


    protected TextView tesla_txt, azimuth_txt, roll_txt, pitch_txt;
    protected SensorManager magSensorManager;
    protected Sensor magSensor;
    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            float azimuth = Math.round(sensorEvent.values[0]); // angular measurement in a spherical coordinate system
            float pitch = Math.round(sensorEvent.values[1]);
            float roll = Math.round(sensorEvent.values[2]);
            double tesla = Math.sqrt((azimuth * azimuth) + (pitch * pitch) + (roll * roll)); // magnetic field strength

            azimuth_txt.setText(Float.toString(azimuth));
            roll_txt.setText(Float.toString(roll));
            pitch_txt.setText(Float.toString(pitch));
            tesla_txt.setText(Double.toString(tesla));

            String entry = azimuth_txt.getText().toString() + ";" + pitch_txt.getText().toString() + ";" + roll_txt.getText().toString() + ";" + tesla_txt.getText().toString() + "\n";
            try {

                File path = new File("/storage/emulated/0/Download");
                File file = new File(path + "/magnetometer.csv");
                FileOutputStream f = new FileOutputStream(file, true);
                try {
                    f.write(entry.getBytes());
                    f.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
   
    Magnetometer() {
    }


}
