package com.example.uisimplu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class StepSensorAcceleration extends StepSensorBase{
    private final String TAG = "StepSensorAcceleration";
    final int valueNum = 5;
    float[] tempValue = new float[valueNum];
    int tempCount = 0;
    boolean isDirectionUp = false;
    int continueUpCount = 0;
    int continueUpFormerCount = 0;
    boolean lastStatus = false;
    float peakOfWave = 0;
    float valleyOfWave = 0;
    long timeOfThisPeak = 0;
    long timeOfLastPeak = 0;
    long timeOfNow = 0;
    float gravityNew = 0;
    float gravityOld = 0;
    final float initialValue = (float) 1.7;
    float ThreadValue = (float) 2.0;

    float minValue = 11f;
    float maxValue = 19.6f;

    private int CountTimeState = 0;
    public static int TEMP_STEP = 0;
    private int lastStep = -1;
    //用x、y、z轴三个维度算出的平均值
    public static float average = 0;
    private Timer timer;

    private long duration = 3500;
    private TimeCount time;

    public StepSensorAcceleration(Context context, StepCallBack stepCallBack) {
        super(context, stepCallBack);
    }


    @Override
    protected void registerStepListener() {

        isAvailable = sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        if (isAvailable) {
            Log.i(TAG, "ACCELEROMETRU DISPONIBIL！");
        } else {
            Log.i(TAG, "ACCELEROMETRU INDISPONIBIL！");
        }
    }

    @Override
    public void unregisterStep() {
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                calc_step(event);
            }
        }
    }
    synchronized private void calc_step(SensorEvent event) {
        average = (float) Math.sqrt(Math.pow(event.values[0], 2)
                + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
        detectorNewStep(average);
    }

    public void detectorNewStep(float values) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            if (DetectorPeak(values, gravityOld)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = System.currentTimeMillis();

                if (timeOfNow - timeOfLastPeak >= 200
                        && (peakOfWave - valleyOfWave >= ThreadValue) && (timeOfNow - timeOfLastPeak) <= 2000) {
                    timeOfThisPeak = timeOfNow;
                    //更新界面的处理，不涉及到算法
                    preStep();
                }
                if (timeOfNow - timeOfLastPeak >= 200
                        && (peakOfWave - valleyOfWave >= initialValue)) {
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = Peak_Valley_Thread(peakOfWave - valleyOfWave);
                }
            }
        }
        gravityOld = values;
    }

    private void preStep() {
//        if (CountTimeState == 0) {
//            // 开启计时器
//            time = new TimeCount(duration, 700);
//            time.start();
//            CountTimeState = 1;
//            Log.v(TAG, "开启计时器");
//        } else if (CountTimeState == 1) {
//            TEMP_STEP++;
//            Log.v(TAG, "计步中 TEMP_STEP:" + TEMP_STEP);
//        } else if (CountTimeState == 2) {
        StepSensorBase.CURRENT_STEP++;
//            if (stepCallBack != null) {
        stepCallBack.Step(StepSensorBase.CURRENT_STEP);
//            }
//        }

    }

    public boolean DetectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }

//        Log.v(TAG, "oldValue:" + oldValue);
        if (!isDirectionUp && lastStatus
                && (continueUpFormerCount >= 2 && (oldValue >= minValue && oldValue < maxValue))) {
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    public float Peak_Valley_Thread(float value) {
        float tempThread = ThreadValue;
        if (tempCount < valueNum) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, valueNum);
            System.arraycopy(tempValue, 1, tempValue, 0, valueNum - 1);
            tempValue[valueNum - 1] = value;
        }
        return tempThread;

    }
    public float averageValue(float[] value, int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / valueNum;
        if (ave >= 8) {
//            Log.v(TAG, "超过8");
            ave = (float) 4.3;
        } else if (ave >= 7) {
//            Log.v(TAG, "7-8");
            ave = (float) 3.3;
        } else if (ave >= 4) {
//            Log.v(TAG, "4-7");
            ave = (float) 2.3;
        } else if (ave >= 3) {
//            Log.v(TAG, "3-4");
            ave = (float) 2.0;
        } else {
//            Log.v(TAG, "else");
            ave = (float) 1.7;
        }
        return ave;
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            StepSensorBase.CURRENT_STEP += TEMP_STEP;
            lastStep = -1;
            Log.v(TAG, "计时正常结束");

            timer = new Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    if (lastStep == StepSensorBase.CURRENT_STEP) {
                        timer.cancel();
                        CountTimeState = 0;
                        lastStep = -1;
                        TEMP_STEP = 0;
                        Log.v(TAG, "停止计步：" + StepSensorBase.CURRENT_STEP);
                    } else {
                        lastStep = StepSensorBase.CURRENT_STEP;
                    }
                }
            };
            timer.schedule(task, 0, 2000);
            CountTimeState = 2;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (lastStep == TEMP_STEP) {
                Log.v(TAG, "onTick 计时停止:" + TEMP_STEP);
                time.cancel();
                CountTimeState = 0;
                lastStep = -1;
                TEMP_STEP = 0;
            } else {
                lastStep = TEMP_STEP;
            }
        }
    }
}
