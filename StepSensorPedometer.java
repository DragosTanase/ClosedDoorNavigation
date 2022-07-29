package com.example.uisimplu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

public class StepSensorPedometer extends StepSensorBase{
    private final String TAG = "StepSensorPedometer";
    private int lastStep = -1;
    private int liveStep = 0;
    private int increment = 0;
    private int sensorMode = 0; // 计步传感器类型
    public StepSensorPedometer(Context context, StepCallBack stepCallBack) {
        super(context, stepCallBack);
    }

    @Override
    protected void registerStepListener() {
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_GAME)) {
            isAvailable = true;
            sensorMode = 0;
            Log.i(TAG, "SENZOR PEDOMETRU DISPONIBIL");
        } else if (sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_GAME)) {
            isAvailable = true;
            sensorMode = 1;
            Log.i(TAG, "SENZOR PEDOMETRU CONTOR DISPONIBIL！");
        } else {
            isAvailable = false;
            Log.i(TAG, "SENZOR PEDOMETRU INDISPONIBIL！");
        }
    }

    @Override
    public void unregisterStep() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        liveStep = (int) event.values[0];
        if (sensorMode == 0) {
            Log.i(TAG, "ETALEPE DETECTORULUI："+liveStep);
            StepSensorBase.CURRENT_STEP += liveStep;
        } else if (sensorMode == 1) {
            Log.i(TAG, "COUNTER PASI："+liveStep);
            StepSensorBase.CURRENT_STEP = liveStep;
        }
        stepCallBack.Step(StepSensorBase.CURRENT_STEP);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
