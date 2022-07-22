package com.example.testsenzorclass;

import android.net.wifi.WifiManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class WifiSignal extends Senzor{


    protected WifiManager wifiManager;
    WifiSignal(){};
    public void displayWifiData(){
        int rssi = wifiManager.getConnectionInfo().getRssi();
        int level = wifiManager.calculateSignalLevel(rssi);
        String ssid = wifiManager.getConnectionInfo().getSSID();

        String entry = "\n" + rssi;

        try {
            writeCSV("/wifiSignal.csv", "RSSI", entry);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
