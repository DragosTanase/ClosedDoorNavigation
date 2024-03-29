package com.example.uisimplu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.FileNotFoundException;

public class Bluetooth extends Senzor {
    protected BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    protected BluetoothManager bluetoothManager;
    protected int rssi;
    protected String address;

    protected final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                address = device.getAddress();

                String entry = "\n"+ device.getAddress() + ", " + String.format("%d", rssi);

                try {
                    writeCSV("/bluetooth.csv", "address" + ", " + "rssi", entry);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    };

}
