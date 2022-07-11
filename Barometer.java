package com.example.barometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;

///// ** MainActivity ** /////
//
//public class MainActivity extends AppCompatActivity {
//
//    Barometer bar = new Barometer();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        bar.txt = findViewById(R.id.txt);
//        bar.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        bar.pressureSensor = bar.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        bar.sensorManager.registerListener(bar.sensorEventListener, bar.pressureSensor, SensorManager.SENSOR_DELAY_UI);
//    }
//
//}

public class Barometer {
    protected TextView txt;
    protected SensorManager sensorManager;
    protected Sensor pressureSensor;
    boolean onePrint = true;

    protected SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float data = sensorEvent.values[0];

            txt.setText(String.format("%.3f mbar", data));
            String once = "data";
            String entry = "\n" + String.format("%.3f", data);

            try {
                File path = new File("/storage/emulated/0/Download");
                File file = new File(path + "/barometer.csv");
                FileOutputStream f = new FileOutputStream(file, true);
                try {
                        f.write(entry.getBytes());
                        f.flush();
                } catch(Exception e){
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
