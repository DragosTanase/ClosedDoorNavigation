package com.example.steps;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ///variables
    private TextView textViewStepCounter,textViewStepCounterSensor;
    private SensorManager sensorManager;
    private Sensor mStepCounter;
    int stepCount=0,checkStep=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){ //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){ //ask for permission
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 0);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        textViewStepCounter=findViewById(R.id.textViewStepCounter);
        textViewStepCounterSensor=findViewById(R.id.textViewStepCounterSensor);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        ///step counting
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor==mStepCounter)
        {
           stepCount=(int)sensorEvent.values[0];
           textViewStepCounter.setText(String.valueOf(stepCount));
           textViewStepCounterSensor.setText("Activat");
            String stepCSV = "\n" + String.valueOf(stepCount);
           try
           {
               File path = new File("/storage/emulated/0/Download");
               File file = new File(path + "/step.csv");
               FileOutputStream f = new FileOutputStream(file, true);
               try {
                   f.write(stepCSV.getBytes());
                   f.flush();
               } catch(Exception e){
                   e.printStackTrace();
               }
           }
           catch(Exception e)
           {
               e.printStackTrace();
           }
           if(checkStep>=stepCount+10)
           {
               ///locatie pe harta
           }
        }
        else
        {
            textViewStepCounterSensor.setText("Dezactivat");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        ///unused
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_UI);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        checkStep=stepCount;
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null) {
            sensorManager.unregisterListener(this, mStepCounter);

        }
    }
}