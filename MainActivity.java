package com.example.mainapp;

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
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements LocationListener{

    StepCounter steps = new StepCounter();
    Accelerometer acc = new Accelerometer();
    WifiSignal wifi = new WifiSignal();
    Barometer bar = new Barometer();
    Gyroscope gyr = new Gyroscope();
    Magnetometer mag = new Magnetometer();
    Bluetooth blu = new Bluetooth();
    GPS gps = new GPS();
    TelephonyManager telephoneManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) { //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) { //ask for permission
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 0);
        }

        gps.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps.txtLat=findViewById(R.id.txtShowCoord);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        gps.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);



        ///StepCounter
        steps.textViewStepCounter = findViewById(R.id.stpCount);
        steps.msensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        steps.mStepCounter = steps.msensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        ///Accelerometer
        acc.txt_X = findViewById(R.id.txt_X);
        acc.txt_Y = findViewById(R.id.txt_Y);
        acc.txt_Z = findViewById(R.id.txt_Z);
        acc.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc.mAccelerator = acc.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        ///Gyroscope
        gyr.txt_X_gyroscope = findViewById(R.id.txt_X_gyroscope);
        gyr.txt_Y_gyroscope = findViewById(R.id.txt_Y_gyroscope);
        gyr.txt_Z_gyroscope = findViewById(R.id.txt_Z_gyroscope);
        gyr.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyr.mGyroscope = gyr.mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        ///Barometer
        bar.txtPressure = findViewById(R.id.txtPressure);
        bar.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        bar.pressureSensor = bar.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        ///Wifi
        wifi.textStrength = findViewById(R.id.textStrength);
        wifi.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ///Magnetometer
        mag.tesla_txt = (TextView) findViewById(R.id.tesla_txt);
        mag.azimuth_txt = (TextView) findViewById(R.id.azimuth_txt);
        mag.roll_txt = (TextView) findViewById(R.id.roll_txt);
        mag.pitch_txt = (TextView) findViewById(R.id.pitch_txt);
        mag.magSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mag.magSensor = mag.magSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        ///Mobile
        MobileData.myPhoneStateListener pslistener = new MobileData.myPhoneStateListener();
        telephoneManager  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephoneManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        SignalStrength signalStrength = telephoneManager.getSignalStrength();
        pslistener.onSignalStrengthsChanged(signalStrength);

        Handler mainHandler = new Handler(Looper.getMainLooper());

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                wifi.displayWifiData();
                mainHandler.postDelayed(this, 1000);
            }
        };
        mainHandler.postDelayed(r, 1);
        //Bluetooth crapa la if
         if (ContextCompat.checkSelfPermission(  MainActivity.this,Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
        {
            blu.BTAdapter.startDiscovery();
        }

        registerReceiver(blu.receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        blu.txt=(TextView)findViewById(R.id.textViewBT);
        Handler bluHandler = new Handler(Looper.getMainLooper());
        final Runnable r2 = new Runnable() {
            @Override
            public void run() {
                bluHandler.postDelayed(this, 1000);
            }
        };


        bluHandler.postDelayed(r2, 1);


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float x = (float) location.getLatitude();
        float y = (float) location.getLongitude();
        gps.txtLat.setText("latitude: " + x * 10000 / 10000 + "\nlongitude " + y * 10000 / 10000);
        try {
            File path = new File("/storage/emulated/0/Download");
            File file = new File(path + "/GPS.csv");
            FileOutputStream f = new FileOutputStream(file, true);
            String once = "latitude" + "," + "longitude";
            String entry = "\n" + String.format("%.6f", x) + "," + String.format("%.3f", y);
            try {
                //print X Y Z once
                if (gps.o == true) {
                    f.write(once.getBytes());
                    f.flush();
                    gps.o = false;
                }
                f.write(entry.getBytes());
                f.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    protected void onResume() {
        super.onResume();
        steps.msensorManager.registerListener(steps.sensorEventListener, steps.mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        acc.mSensorManager.registerListener(acc.sensorEventListener, acc.mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);
        bar.sensorManager.registerListener(bar.sensorEventListener, bar.pressureSensor, SensorManager.SENSOR_DELAY_UI);
        gyr.mSensorManager.registerListener(gyr.sensorEventListener, gyr.mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mag.magSensorManager.registerListener(mag.sensorEventListener, mag.magSensor, SensorManager.SENSOR_DELAY_NORMAL);
        registerReceiver(blu.receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

    }

    @Override
    protected void onPause() {
        super.onPause();
        steps.msensorManager.unregisterListener(steps.sensorEventListener, steps.mStepCounter);
    }


}