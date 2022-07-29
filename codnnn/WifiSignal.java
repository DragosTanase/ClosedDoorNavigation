package com.example.uisimplu;

import android.net.wifi.WifiManager;
import java.io.FileNotFoundException;


public class WifiSignal extends Senzor{


    protected WifiManager wifiManager;
    protected int rssi;
    protected  int level;
    WifiSignal(){};
    public void displayWifiData(){
        rssi = wifiManager.getConnectionInfo().getRssi();
        level = wifiManager.calculateSignalLevel(rssi);
        String ssid = wifiManager.getConnectionInfo().getSSID();

        String entry = "\n" + rssi;

        try {
            writeCSV("/wifiSignal.csv", "RSSI", entry);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

