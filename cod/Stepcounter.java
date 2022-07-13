package com.example.accelerator;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;


import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;

/*
    public class MainActivity extends AppCompatActivity {
    ///variables
    private TextView textViewStepCounter,textViewStepCounterSensor;
    private SensorManager sensorManager;
    private Sensor mStepCounter;
    int stepCount=0,checkStep=0;
    Stepcounter step= new Stepcounter();
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

        step.sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        step.mStepCounter=step.sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

    }
 */

public class Stepcounter {

    protected SensorManager msensorManager;
    protected Sensor mStepCounter;
    protected TextView textViewStepCounter;



    Stepcounter(){};

    protected SensorEventListener sensorEventListener = new SensorEventListener(){


        @Override
        public void onSensorChanged(@NonNull SensorEvent sensorEvent) {
            float stepCount = sensorEvent.values[0];
            textViewStepCounter.setText("PASI: " + sensorEvent.values[0]);
            String stepCSV = "\n" + String.valueOf(stepCount);


                try {
                    File path = new File("/storage/emulated/0/Download");
                    File file = new File(path + "/step.csv");
                    FileOutputStream f = new FileOutputStream(file, true);
                    try {

                            f.write(stepCSV.getBytes());

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
