package com.example.uisimplu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements LocationListener, OrientSensor.OrientCallBack{
    private static final String TAG = "DA";
    Accelerometer accelerometer = new Accelerometer();
    WifiSignal wifiSignal = new WifiSignal();
    Gyroscope gyroscope = new Gyroscope();
    Magnetometer magnetometer = new Magnetometer();
    Barometer barometer = new Barometer();
    Bluetooth bluetooth = new Bluetooth();
    StepCounter stepCounter = new StepCounter();
    Senzor senzor = new Senzor();


    MobileData mobileData = new MobileData();
    GPS gps = new GPS();
    protected boolean ok = true;
    private Canvas mCanvas;
    private int mStepLen = 50; // marimea pasului
    private OrientSensor mOrientSensor;

    @Override
    public void Orient(int orient) {
        // 方向回调


//       orient = SensorUtil.getInstance().getRotateEndOrient(orient);
        mCanvas.autoDrawArrow(orient);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCanvas = (Canvas) findViewById(R.id.step_surfaceView);
        Senzor.getInstance().printAllSensor(this);
        mOrientSensor = new OrientSensor(this, this);
        if (!mOrientSensor.registerOrient()) {
            Toast.makeText(this, "orientu nu e disponibil！", Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG, "orientu ESTE disponibil");
        }
        gps.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        barometer.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        barometer.pressureSensor = barometer.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        wifiSignal.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        accelerometer.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer.mAccelerator = accelerometer.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope.mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        gyroscope.mGyroscope = gyroscope.mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer.magSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetometer.magSensor = magnetometer.magSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        stepCounter.msensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounter.mStepCounter=stepCounter.msensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        MobileData.myPhoneStateListener pslistener = mobileData.new myPhoneStateListener();
        mobileData.telephoneManager  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){ //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){ //ask for permission
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 0);
        }
        if (ContextCompat.checkSelfPermission(  MainActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
        {
            bluetooth.BTAdapter.startDiscovery();
        }

        Handler mainHandler2 = new Handler(Looper.getMainLooper());
        final Runnable[] r4 = new Runnable[1];

        Button startBtn = findViewById(R.id.buttonStart);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ok) {
                    startBtn.setText("STOP");
                    ok = false;


                    accelerometer.mSensorManager.registerListener(accelerometer.sensorEventListener, accelerometer.mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);
                    stepCounter.msensorManager.registerListener(stepCounter.sensorEventListener, stepCounter.mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
                    barometer.sensorManager.registerListener(barometer.sensorEventListener, barometer.pressureSensor, SensorManager.SENSOR_DELAY_UI);
                    gyroscope.mSensorManager.registerListener(gyroscope.sensorEventListener, gyroscope.mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                    magnetometer.magSensorManager.registerListener(magnetometer.sensorEventListener, magnetometer.magSensor, SensorManager.SENSOR_DELAY_NORMAL);

                    //Bluetooth
                    registerReceiver(bluetooth.receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                    Handler bluHandler = new Handler(Looper.getMainLooper());
                    final Runnable r2 = new Runnable() {
                        @Override
                        public void run() {
                            bluHandler.postDelayed(this, 1000);
                        }
                    };
                    bluHandler.postDelayed(r2, 1000);

                    //GPS
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                    gps.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) MainActivity.this);

                    //Mobile Data
                    mobileData.telephoneManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                    SignalStrength signalStrength = mobileData.telephoneManager.getSignalStrength();
                    pslistener.onSignalStrengthsChanged(signalStrength);

                    //Wifi
                    r4[0] = new Runnable() {
                        @Override
                        public void run() {
                            wifiSignal.displayWifiData();
                            mainHandler2.postDelayed(this, 100);
                        }
                    };
                    mainHandler2.postDelayed(r4[0], 100);

                    //save in csv start value
                    Handler startRecHandler = new Handler(Looper.getMainLooper());
                    startRecHandler.postDelayed(new Runnable(){
                        public void run(){
                            Log.d("handler","save data start");
                            String startEntry = "data_pres, x_acc, y_acc, z_acc, x_gyro, y_gyro, z_gyro, mag_tesla, wifi_rssi, steps" + "\n" + barometer.pressure + "," + accelerometer.xAccelerometer + "," + accelerometer.yAccelerometer + "," + accelerometer.zAccelerometer + "," + gyroscope.xGyroscope + "," + gyroscope.yGyroscope + "," + gyroscope.zGyroscope + "," + magnetometer.tesla + "," + wifiSignal.rssi + "," + stepCounter.stepCount;
                            try {
                                senzor.writeCSV("/start.csv", "", startEntry);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    },200);

                } else {
                    startBtn.setText("START");
                    ok = true;

                    accelerometer.mSensorManager.unregisterListener(accelerometer.sensorEventListener, accelerometer.mAccelerator);
                    stepCounter.msensorManager.unregisterListener(stepCounter.sensorEventListener, stepCounter.mStepCounter);
                    barometer.sensorManager.unregisterListener(barometer.sensorEventListener, barometer.pressureSensor);
                    gyroscope.mSensorManager.unregisterListener(gyroscope.sensorEventListener, gyroscope.mGyroscope);
                    magnetometer.magSensorManager.unregisterListener(magnetometer.sensorEventListener, magnetometer.magSensor);
                    gps.locationManager.removeUpdates((LocationListener) MainActivity.this);
                    mobileData.telephoneManager.listen(pslistener,PhoneStateListener.LISTEN_NONE);

                    //Bluetooth
                    unregisterReceiver(bluetooth.receiver);
                    Handler bluHandler = new Handler(Looper.getMainLooper());
                    final Runnable r2 = new Runnable() {
                        @Override
                        public void run() {
                            bluHandler.postDelayed(this, 1000);
                        }
                    };
                    bluHandler.postDelayed(r2, 1000);
                    bluHandler.removeCallbacks(r2);

                    //Wifi
                    mainHandler2.removeCallbacks(r4[0]);

                    Log.d("handler","save data stop");
                    String stopEntry = "data_pres, x_acc, y_acc, z_acc, x_gyro, y_gyro, z_gyro, mag_tesla, wifi_rssi, steps" + "\n" + barometer.pressure + "," + accelerometer.xAccelerometer + "," + accelerometer.yAccelerometer + "," + accelerometer.zAccelerometer + "," + gyroscope.xGyroscope + "," + gyroscope.yGyroscope + "," + gyroscope.zGyroscope + "," + magnetometer.tesla + "," + wifiSignal.rssi + "," + stepCounter.stepCount;
                    try {
                        senzor.writeCSV("/stop.csv", "", stopEntry);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    ///// GPS LATITUDE AND LONGITUDE VALUES //////////
    @Override
    public void onLocationChanged(@NonNull Location location) {
        float latitude = (float)location.getLatitude();
        float longitude =  (float)location.getLongitude();

        String entry = "\n" + String.format("%.4f", latitude) + "," + String.format("%.4f", longitude);
        try {
            senzor.writeCSV("/gps.csv", "latitude" + ", " + "longitude", entry);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}