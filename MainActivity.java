package com.example.stepcounter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ///variables
    private TextView textViewStepCounter,textViewStepCounterSensor,textViewStepDetectorSensor;
    private SensorManager sensorManager;
    private Sensor mStepCounter,mStepDetector;
    private boolean isDetectorSensor,isCounterSensor;
    int stepCount=0,stepDetected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        textViewStepCounter=findViewById(R.id.textViewStepCounter);
        textViewStepCounterSensor=findViewById(R.id.textViewStepCounterSensor);
        textViewStepDetectorSensor=findViewById(R.id.textViewStepDetectorSensor);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        ///step counting
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            textViewStepCounterSensor.setText("ON");
            isCounterSensor=true;
        }
        else
        {
            textViewStepCounterSensor.setText("OFF");
            isCounterSensor=false;
        }
        ///step detector
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
        {
            mStepDetector=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            textViewStepDetectorSensor.setText("ON");
            isDetectorSensor=true;
        }
        else
        {
            textViewStepDetectorSensor.setText("OFF");
            isDetectorSensor=false;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor==mStepCounter)
        {
            stepCount=(int)sensorEvent.values[0];
            textViewStepCounter.setText(String.valueOf(stepCount));
        }
        else if(sensorEvent.sensor==mStepDetector)
        {
            stepDetected=(int)(stepDetected+sensorEvent.values[0]);
            textViewStepDetectorSensor.setText(String.valueOf(stepDetected));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        ///not used
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
        {
            sensorManager.registerListener(this,mStepDetector,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
        {
            sensorManager.unregisterListener(this,mStepCounter);
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null)
        {
            sensorManager.unregisterListener(this,mStepDetector);
        }
    }
}