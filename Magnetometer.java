package com.example.magnetometer;

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
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;


import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Magnetometer {


          ///////////////// MAIN ACTIVITY ////////////////
    /*
      Magnetometer mag = new Magnetometer();

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mag.tesla_txt = (TextView) findViewById(R.id.tesla_txt);
        mag.azimuth_txt = (TextView) findViewById(R.id.azimuth_txt);
        mag.roll_txt = (TextView) findViewById(R.id.roll_txt);
        mag.pitch_txt = (TextView) findViewById(R.id.pitch_txt);


        mag.magSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mag.magSensor = mag.magSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mag.magSensorManager.registerListener(mag.sensorEventListener, mag.magSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    */

    protected TextView tesla_txt, azimuth_txt, roll_txt, pitch_txt;
    protected SensorManager magSensorManager;
    protected Sensor magSensor;

    Magnetometer() {
    }

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

                File path = new File("/storage/emulated/0/Documents");
                File file = new File(path + "/magnetometer2_values.csv");
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
}
