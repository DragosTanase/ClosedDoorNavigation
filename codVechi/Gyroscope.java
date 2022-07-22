package com.example.mainapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class Gyroscope {
    protected TextView txt_X_gyroscope, txt_Y_gyroscope, txt_Z_gyroscope;
    protected SensorManager mSensorManager;
    protected Sensor mGyroscope;
    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];


            txt_X_gyroscope.setText("X: " + x);
            txt_Y_gyroscope.setText("Y: " + y);
            txt_Z_gyroscope.setText("Z: " + z);


            String entry = "\n" + String.format("%.2f", x) + "," + String.format("%.2f", y) + "," + String.format("%.2f", z);

            try {

                File path = new File("/storage/emulated/0/Download");
                File file = new File(path + "/gyroscope.csv");
                FileOutputStream f = new FileOutputStream(file, true);
                try {
                    if (x != 0.00 && y != 0.00) {
                        f.write(entry.getBytes());
                        f.flush();
                    }

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

    public Gyroscope() {
    }


}
