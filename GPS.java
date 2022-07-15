package com.example.gpsapp;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;

public class GPS{

   ///////////////// MAIN ACTIVITY ////////////////
   /*
    GPS gps = new GPS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps.txtLat=findViewById(R.id.txtShowCoord);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        gps.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float x = (float)location.getLatitude();
        float y=  (float)location.getLongitude();
        gps.txtLat.setText("latitude: " + x*10000/10000 +"\nlongitude " + y*10000/10000);
        try {

            File path = new File("/storage/emulated/0/Download");
            File file = new File(path + "/GPS.csv");
            FileOutputStream f = new FileOutputStream(file, true);
            String once = "latitude" + "," + "longitude";
            String entry = "\n" + String.format("%.6f", x) + "," + String.format("%.3f", y);
            try {
                //print X Y Z once
                if(gps.o==true){
                    f.write(once.getBytes());
                    f.flush();
                    gps.o=false;
                }
                f.write(entry.getBytes());
                f.flush();

            }catch(Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
   protected LocationManager locationManager;
   protected TextView txtLat;
   protected Location location;
   protected boolean o = true;
   GPS(){};
}


