package com.example.mainapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;

public class Bluetooth {

    /* /////////////// MAIN ACTIVITY //////////////// */

    /*

package com.example.blutut;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;



public class MainActivity extends AppCompatActivity {

    Bluetooth blu = new Bluetooth();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        registerReceiver(blu.receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        blu.txt = findViewById(R.id.textView);


        Handler bluHandler = new Handler(Looper.getMainLooper());
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                bluHandler.postDelayed(this, 1000);
            }
        };

        if (ContextCompat.checkSelfPermission(  MainActivity.this,Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
        {
            blu.BTAdapter.startDiscovery();
        }

        bluHandler.postDelayed(r, 1);
    }

    protected void onResume() {
        super.onResume();

        registerReceiver(blu.receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }
}


     */
    protected BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    protected TextView txt;
    protected BluetoothManager bluetoothManager;


    protected final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String address = device.getAddress();
                txt.setText(txt.getText() + " => " + rssi + "dBm\n");

                String entry = address + "," + String.format("%d", rssi) + "\n";

                try {
                    File path = new File("/storage/emulated/0/Download");
                    File file = new File(path + "/bluetooth.csv");
                    FileOutputStream f = new FileOutputStream(file, true);
                    try {
                        f.write(entry.getBytes());
                        f.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    };

}
