package com.example.proiectgyroscope;


import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import android.os.Bundle;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;


public class Gyroscope {
    protected TextView txt_X, txt_Y, txt_Z;
    protected SensorManager mSensorManager;
    protected Sensor mGyroscope;
    boolean o = true;

    //MAIN

    public Gyroscope() {
    }




//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        txt_X=findViewById(R.id.txt_X);
//        txt_Y=findViewById(R.id.txt_Y);
//        txt_Z=findViewById(R.id.txt_Z);
//
//        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
//        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//
//    }
//
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(sensorEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
//    }


    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];


            txt_X.setText("X: " + x);
            txt_Y.setText("Y: " + y);
            txt_Z.setText("Z: " + z);


            String once = "X" + "," + "Y" + "," + "Z";
            String entry = "\n" + String.format("%.2f", x) + "," + String.format("%.2f", y) + "," + String.format("%.2f", z);

            try {

                File path = new File("/storage/emulated/0/Download");
                File file = new File(path + "/fisier.txt");
                FileOutputStream f = new FileOutputStream(file, true);
                try {

                    //print X Y Z once
                    if(o==true){
                        f.write(once.getBytes());
                        f.flush();
                        o=false;
                    }

                    if(x!=0.00 && y!=9.81){
                        f.write(entry.getBytes());
                        f.flush();
                    }

                }catch(Exception e){
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
