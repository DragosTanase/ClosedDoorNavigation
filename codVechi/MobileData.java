package com.example.mainapp;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;


public class MobileData extends AppCompatActivity{

    TelephonyManager TelephoneManager;
    myPhoneStateListener pslistener;

    public static class myPhoneStateListener extends PhoneStateListener{
        float signalStrengthValue = 0;
        public void onSignalStrengthsChanged(SignalStrength signalStrength){
            this.signalStrengthValue = signalStrength.getCellSignalStrengths().get(0).getDbm();
            String entry = "\n" + String.format("%.3f dBm", this.signalStrengthValue);
            try {
                File path = new File("/storage/emulated/0/Download");
                File file = new File(path + "/GSM.csv");
                FileOutputStream f = new FileOutputStream(file, true);
                try {

                    f.write(entry.getBytes());
                    f.flush();

                }catch(Exception e){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
