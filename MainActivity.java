package com.example.testsenzorclass;

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

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements LocationListener{
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        gps.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

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

        
       
        MobileData.myPhoneStateListener pslistener = mobileData.new myPhoneStateListener();
        mobileData.telephoneManager  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mobileData.telephoneManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        SignalStrength signalStrength = mobileData.telephoneManager.getSignalStrength();
        pslistener.onSignalStrengthsChanged(signalStrength);

        registerReceiver(bluetooth.receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        Handler bluHandler = new Handler(Looper.getMainLooper());
        final Runnable r2 = new Runnable() {
            @Override
            public void run() {
                bluHandler.postDelayed(this, 1000);
            }
        };


        bluHandler.postDelayed(r2, 1000);


        Handler mainHandler = new Handler(Looper.getMainLooper());

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                wifiSignal.displayWifiData();
                mainHandler.postDelayed(this, 1000);
            }
        };
        
        mainHandler.postDelayed(r, 1000);
    }
    protected void onResume() {
        super.onResume();
        accelerometer.mSensorManager.registerListener(accelerometer.sensorEventListener, accelerometer.mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);
        gyroscope.mSensorManager.registerListener(gyroscope.sensorEventListener, gyroscope.mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        magnetometer.magSensorManager.registerListener(magnetometer.sensorEventListener, magnetometer.magSensor, SensorManager.SENSOR_DELAY_NORMAL);
        barometer.sensorManager.registerListener(barometer.sensorEventListener, barometer.pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        registerReceiver(bluetooth.receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        stepCounter.msensorManager.registerListener(stepCounter.sensorEventListener, stepCounter.mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        stepCounter.msensorManager.unregisterListener(stepCounter.sensorEventListener, stepCounter.mStepCounter);

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
