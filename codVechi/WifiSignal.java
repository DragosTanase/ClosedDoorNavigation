package com.example.mainapp;

import android.net.wifi.WifiManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class WifiSignal {
    protected TextView textStrength;
    protected WifiManager wifiManager;

    /////////////////// MAIN ACTIVITY ON CREATE ///////////////////
    /*
     wifi.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //for WIFI signal to run continously
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                wifi.displayWifiData();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1);
     */

    public void displayWifiData() {
        int rssi = wifiManager.getConnectionInfo().getRssi();
        int level = wifiManager.calculateSignalLevel(rssi);
       // WifiManager.calculateSignalLevel(rssi,5);

        String ssid = wifiManager.getConnectionInfo().getSSID();
        textStrength.setText("Signal strength: " + level + "\nRSSI: " + rssi);

        String entry = "\n" + rssi + "," + level;

        try {
            File path = new File("/storage/emulated/0/Download");
            File file = new File(path + "/wifiSignal.csv");
            FileOutputStream f = new FileOutputStream(file, true);
            f.write(entry.getBytes());
            f.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
