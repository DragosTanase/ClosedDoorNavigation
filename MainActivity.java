package com.example.aplicatieactuala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationListener, OrientSensor.OrientCallBack, StepSensorBase.StepCallBack{
    private static final String TAG = "DA";
    Accelerometer accelerometer = new Accelerometer();
    WifiSignal wifiSignal = new WifiSignal();
    Gyroscope gyroscope = new Gyroscope();
    Magnetometer magnetometer = new Magnetometer();
    Barometer barometer = new Barometer();
    Bluetooth bluetooth = new Bluetooth();
    StepCounter stepCounter = new StepCounter();
    Senzor senzor = new Senzor();
    protected StepSensorBase mStepSensor;
    private Spinner spinner;
    protected Button buttonPP;
    int widthCanvas = 0;
    MobileData mobileData = new MobileData();
    GPS gps = new GPS();
    protected boolean ok = true;
    protected Canvas mCanvas;
    private int mStepLen = 40; // marimea pasului
    protected OrientSensor mOrientSensor;
    float xDown=0; float yDown=0;
    public MainActivity() throws FileNotFoundException {
    }

    @Override
    public void Step(int stepNum) {
        mCanvas.autoAddPoint(mStepLen);
    }

    @Override
    public float Orient(double orient) {
        mCanvas.autoDrawArrow((int)orient);
        return 0;
    }

    public void OrientClick(){
        mOrientSensor = new OrientSensor(this, this);
        if (!mOrientSensor.registerOrient()) {
            Toast.makeText(this, "orientul nu e disponibil！", Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG, "orientul ESTE disponibil");
        }

    }

    public void StepClick()
    {
        mStepSensor = new StepSensorAcceleration(this, this);
        if (!mStepSensor.registerStep()) {
            Log.i(TAG, "mSTEPSNESOR NU ESTE disponibil");
        }else{
            Log.i(TAG, "mSTEPSNESOR ESTE disponibil");
        }
    }


    protected void spinnerList()
    {
        ArrayList<String> list= new ArrayList<>();
        File f1 = new File("/storage/emulated/0/Download/PinPoints.csv");
        List<String> lista;
        try {
            lista = Files.readAllLines(f1.toPath(), StandardCharsets.UTF_8);
            for(String l: lista){
                String [] array=l.split(",",-1);
                if(array[array.length-1].equals("denumire"))
                {
                    continue;
                }else
                {
                    list.add(array[array.length - 1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //SPINNER PP
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner=findViewById(R.id.spinnerPP);
        mCanvas =  findViewById(R.id.step_surfaceView);
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
//        MobileData.myPhoneStateListener pslistener = mobileData.new myPhoneStateListener();

        Handler mainHandler2 = new Handler(Looper.getMainLooper());
        final Runnable[] r4 = new Runnable[1];

        spinnerList();
        spinner.setOnTouchListener(spinnerOnTouch);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //PIN POINT

        buttonPP =  findViewById(R.id.buttonPP);
        buttonPP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String pinEntry ="\n"+accelerometer.xAccelerometer + "," + accelerometer.yAccelerometer + "," + accelerometer.zAccelerometer + "," + gyroscope.xGyroscope + "," + gyroscope.yGyroscope + "," + gyroscope.zGyroscope + "," + magnetometer.tesla + "," + wifiSignal.rssi+",";
                openDialog(pinEntry);

            }
            public void openDialog(String pinEntry) {
                PopUpWindow exampleDialog = new PopUpWindow();
                exampleDialog.show(getSupportFragmentManager(), "example dialog");
                Log.wtf("Pop-UP:" ,exampleDialog.getEntry());
                exampleDialog.setData(pinEntry);
                exampleDialog.setmCanvas(mCanvas);
            }
        });

        Button startBtn = findViewById(R.id.buttonStart);
             startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ok) {
                    startBtn.setText("STOP");
                    ok = false;

                    OrientClick();
                    StepClick();
                    accelerometer.mSensorManager.registerListener(accelerometer.sensorEventListener, accelerometer.mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);
                    stepCounter.msensorManager.registerListener(stepCounter.sensorEventListener, stepCounter.mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
                    barometer.sensorManager.registerListener(barometer.sensorEventListener, barometer.pressureSensor, SensorManager.SENSOR_DELAY_UI);
                    gyroscope.mSensorManager.registerListener(gyroscope.sensorEventListener, gyroscope.mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                    magnetometer.magSensorManager.registerListener(magnetometer.sensorEventListener, magnetometer.magSensor, SensorManager.SENSOR_DELAY_NORMAL);
//                    mobileData.telephoneManager  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

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

//                    //Mobile Data
//                    mobileData.telephoneManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
//                    SignalStrength signalStrength = mobile+Data.telephoneManager.getSignalStrength();
//                    pslistener.onSignalStrengthsChanged(signalStrength);

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
                            String startHeader= "data_pres, x_acc, y_acc, z_acc, x_gyro, y_gyro, z_gyro, mag_tesla, wifi_rssi, steps" + "\n";
                            String startEntry =  barometer.pressure + "," + accelerometer.xAccelerometer + "," + accelerometer.yAccelerometer + "," + accelerometer.zAccelerometer + "," + gyroscope.xGyroscope + "," + gyroscope.yGyroscope + "," + gyroscope.zGyroscope + "," + magnetometer.tesla + "," + wifiSignal.rssi + "," + stepCounter.stepCount+"\n";
                            try {
                                senzor.writeCSV("/start.csv", startHeader, startEntry);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    },200);



                    r5[0] = new Runnable() {
                        @Override
                        public void run() {
                           routeHandler.postDelayed(this,1000);
                            //route file writing data
                            String onceRoute  = "\n"+ "accelerometer X," + " accelerometer Y, "+ "accelerometer Z, " +
                                    "gyroscope X, " + "gyroscope Y, " + "gyroscope Z, " +
                                    "pressure, " +
                                    "magnetometer azimuth, " + "magnetometer pitch, " + "magnetometer roll, " + "magnetometer tesla, " +
                                    "Bluetooth address, "+ "Bluetooth Rssi, " +
                                    "GPS latitude, "+ "GPS longitude, " +
                                    "Wifi rssi, " +
                                    "steps";

                            String routeEntry = "\n" + accelerometer.xAccelerometer + "," +
                                    accelerometer.yAccelerometer + "," +
                                    accelerometer.zAccelerometer + "," +

                                    gyroscope.xGyroscope + "," +
                                    gyroscope.yGyroscope + "," +
                                    gyroscope.zGyroscope + "," +

                                    barometer.pressure + "," +

                                    magnetometer.azimuth + "," +
                                    magnetometer.pitch + "," +
                                    magnetometer.roll + "," +
                                    magnetometer.tesla + "," +

                                    bluetooth.address + "," +
                                    bluetooth.rssi + "," +

                                    gps.latitude + "," +
                                    gps.longitude + "," +

                                    wifiSignal.rssi + "," +

                                    stepCounter.stepCount;

                            try {

                                senzor.writeCSV("/Route.csv",onceRoute,routeEntry);
                            }

                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                           routeHandler.postDelayed(this, 1000);
                        }
                    };
                    routeHandler.postDelayed(r5[0], 1000);




                } else {
                    startBtn.setText("START");
                    ok = true;

                    accelerometer.mSensorManager.unregisterListener(accelerometer.sensorEventListener, accelerometer.mAccelerator);
                    stepCounter.msensorManager.unregisterListener(stepCounter.sensorEventListener, stepCounter.mStepCounter);
                    barometer.sensorManager.unregisterListener(barometer.sensorEventListener, barometer.pressureSensor);
                    gyroscope.mSensorManager.unregisterListener(gyroscope.sensorEventListener, gyroscope.mGyroscope);
                    magnetometer.magSensorManager.unregisterListener(magnetometer.sensorEventListener, magnetometer.magSensor);
                    gps.locationManager.removeUpdates((LocationListener) MainActivity.this);
                    mOrientSensor.unregisterOrient();
                    mStepSensor.unregisterStep();
//                    mobileData.telephoneManager.listen(pslistener,PhoneStateListener.LISTEN_NONE);

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
                    String stopHeader="data_pres, x_acc, y_acc, z_acc, x_gyro, y_gyro, z_gyro, mag_tesla, wifi_rssi, steps" + "\n";
                    String stopEntry = barometer.pressure + "," + accelerometer.xAccelerometer + "," + accelerometer.yAccelerometer + "," + accelerometer.zAccelerometer + "," + gyroscope.xGyroscope + "," + gyroscope.yGyroscope + "," + gyroscope.zGyroscope + "," + magnetometer.tesla + "," + wifiSignal.rssi + "," + stepCounter.stepCount+"\n";
                    try {
                        senzor.writeCSV("/stop.csv", stopHeader, stopEntry);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //handler to stop writing in the route file
                    routeHandler.removeCallbacks(r5[0]);
                }
            }
            
        });

        mCanvas.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override


            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()){

                    // the user put his finger down on image
                    case MotionEvent.ACTION_DOWN:
                        xDown = event.getX();
                        yDown = event.getY();
                        break;

                    //the user move his finger
                    case MotionEvent.ACTION_MOVE:
                        float movedX, movedY;
                        movedX = event.getX();
                        movedY = event.getY();

                        //calculate how much the user moved his finger
                        float distanceX = movedX - xDown;
                        float distanceY = movedY - yDown;

                        //move the view to that position
                        mCanvas.setX(mCanvas.getX() + distanceX);
                        mCanvas.setY(mCanvas.getY() + distanceY);

//                        //for next move event
//                        xDown = movedX;
//                        yDown = movedY;
                        break;
                }
                return true;
            }
        });

    }




    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener()
    {

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                spinnerList();
            }
            return false;
        }
    };



    @Override
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
