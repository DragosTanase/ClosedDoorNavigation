package com.example.accelerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Stepcounter steps = new Stepcounter();
    Accelerometer acc = new Accelerometer();
    WifiSignal wifi = new WifiSignal();
    Barometer bar = new Barometer();
    Gyroscope gyr = new Gyroscope();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){ //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){ //ask for permission
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 0);
        }
        steps.textViewStepCounter=findViewById(R.id.stpCount);
        acc.txt_X=findViewById(R.id.txt_X);
        acc.txt_Y=findViewById(R.id.txt_Y);
        acc.txt_Z=findViewById(R.id.txt_Z);
        gyr.txt_X_gyroscope=findViewById(R.id.txt_X_gyroscope);
        gyr.txt_Y_gyroscope=findViewById(R.id.txt_Y_gyroscope);
        gyr.txt_Z_gyroscope=findViewById(R.id.txt_Z_gyroscope);
        bar.txtPressure = findViewById(R.id.txtPressure);
        wifi.textStrength=findViewById(R.id.textStrength);


        wifi.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        acc.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        acc.mAccelerator = acc.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        bar.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        bar.pressureSensor = bar.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        gyr.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        gyr.mGyroscope = gyr.mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        steps.msensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        steps.mStepCounter=steps.msensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);



        Handler handler = new Handler();
        //for WIFI signal to run continously
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                wifi.displayWifiData();
                handler.postDelayed(this, 1000);
            }

        };

        handler.postDelayed(r, 1000);

    }


    protected void onResume() {
        super.onResume();
        steps.msensorManager.registerListener(steps.sensorEventListener, steps.mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        acc.mSensorManager.registerListener(acc.sensorEventListener, acc.mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);
        bar.sensorManager.registerListener(bar.sensorEventListener, bar.pressureSensor, SensorManager.SENSOR_DELAY_UI);
        gyr.mSensorManager.registerListener(gyr.sensorEventListener, gyr.mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause(){
            super.onPause();
            steps.msensorManager.unregisterListener(steps.sensorEventListener, steps.mStepCounter);
    }




}