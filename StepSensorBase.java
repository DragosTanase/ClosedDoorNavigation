package com.example.uisimplu;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class StepSensorBase implements SensorEventListener {
    private final Context context;
    protected StepCallBack stepCallBack;
    protected SensorManager sensorManager;
    protected static int CURRENT_STEP = 0;
    protected boolean isAvailable = false;

    public StepSensorBase(Context context, StepCallBack stepCallBack) {
        this.context = context;
        this.stepCallBack = stepCallBack;
    }

    public interface StepCallBack {

        void Step(int stepNum);
    }

    public boolean registerStep() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }

        sensorManager = Senzor.getInstance().getSensorManager(context);
        registerStepListener();
        return isAvailable;
    }


    protected abstract void registerStepListener();

    public abstract void unregisterStep();

}
