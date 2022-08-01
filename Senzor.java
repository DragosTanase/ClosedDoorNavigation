package com.example.uisimplu;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.Stack;

public class Senzor {
    protected File path = new File("/storage/emulated/0/Download");
    protected File file;
    protected FileOutputStream f;
    boolean headerCSV = true;
    private static final String TAG = "SENZORI: ";

    private static final int SENSE = 10; // 方向差值灵敏度
    private static final int STOP_COUNT = 6; // 停止次数
    private int initialOrient = -1; // 初始方向
    private int endOrient = -1; // 转动停止方向

    private boolean isRotating = false; // 是否正在转动
    private int lastDOrient = 0; // 上次方向与初始方向差值
    private Stack<Integer> dOrientStack = new Stack<>();

    Senzor(){};
    private static final Senzor sensorUtil = new Senzor(); //constanta Singleton
    private SensorManager sensorManager;

    public static Senzor getInstance() {
        return sensorUtil;
    }

    public SensorManager getSensorManager(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        return sensorManager;
    }


    public int getRotateEndOrient(int orient) {
        if (initialOrient == -1) {
            // 初始化转动
            endOrient = initialOrient = orient;
            Log.i(TAG, "getRotateEndOrient: 初始化，方向：" + initialOrient);
        }

        int currentDOrient = Math.abs(orient - initialOrient);
        if (!isRotating) {

            lastDOrient = currentDOrient;
            if (lastDOrient >= SENSE) {

                isRotating = true;
            }
        } else {

            if (currentDOrient <= lastDOrient) {

                int size = dOrientStack.size();
                if (size >= STOP_COUNT) {

                    for (int i = 0; i < size; i++) {
                        if (Math.abs(currentDOrient - dOrientStack.pop()) >= SENSE) {
                            isRotating = true;
                            break;
                        }
                        isRotating = false;
                    }
                }

                if (!isRotating) {

                    dOrientStack.clear();
                    initialOrient = -1;
                    endOrient = orient;
                    Log.i(TAG, "getRotateEndOrient: ------停止转动，方向：" + endOrient);
                } else {

                    dOrientStack.push(currentDOrient);
                    Log.i(TAG, "getRotateEndOrient: 正在转动，方向：" + orient);
                }
            } else {
                lastDOrient = currentDOrient;
            }
        }
        return endOrient;
    }

    public void writeCSV(String fileName, String once, String entry) throws FileNotFoundException {

        file = new File(path + fileName);
        f = new FileOutputStream(file, true);
        if(file.length()!=0)
        {
            try {
                f.write(entry.getBytes());
                f.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                f.write(once.getBytes());
                f.write(entry.getBytes());
                f.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
