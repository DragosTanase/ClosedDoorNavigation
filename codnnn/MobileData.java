package com.example.uisimplu;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import java.io.FileNotFoundException;

public class MobileData extends Senzor{
    protected TelephonyManager telephoneManager;
    protected myPhoneStateListener pslistener;

    public class myPhoneStateListener extends PhoneStateListener{
        float signalStrengthValue = 0;

        public void onSignalStrengthsChanged(SignalStrength signalStrength){
            this.signalStrengthValue = signalStrength.getCellSignalStrengths().get(0).getDbm();
            String entry = "\n" + String.format("%.3f", this.signalStrengthValue);
            try {
                writeCSV("/mobileDATA.csv", "dbm", entry);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
