package com.example.uisimplu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class OrientSensor implements SensorEventListener {
    private static final String TAG = "OrientSensor";
    private SensorManager sensorManager;
    private final OrientCallBack orientCallBack;
    private final Context context;
    float[] accelerometerValues = new float[3];
    float[] magneticValues = new float[3];

    public OrientSensor(Context context, OrientCallBack orientCallBack) {
        this.context = context;
        this.orientCallBack = orientCallBack;
    }

    public interface OrientCallBack {

        void Orient(int orient);
    }


    public Boolean registerOrient() {
        boolean isAvailable = true;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // 注册加速度传感器
        if (sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME)) {
            Log.i(TAG, "accelerometru disponibil！");
        } else {
            Log.i(TAG, "accelerometru INDISPONIBIL！");
            isAvailable = false;
        }

        // 注册地磁场传感器
        if (sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME)) {
            Log.i(TAG, "magnetic filed disponibil！");
        } else {
            Log.i(TAG, "magnetic filed nedisponibil！");
            isAvailable = false;
        }
        return isAvailable;
    }


    public void unregisterOrient() {
        sensorManager.unregisterListener(this);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values.clone();
        }

        float[] R = new float[9];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
        SensorManager.getOrientation(R, values);
        int degree = (int) Math.toDegrees(values[0]);//旋转角度
        if (degree < 0) {
            degree += 360;
        }
        orientCallBack.Orient(degree);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
