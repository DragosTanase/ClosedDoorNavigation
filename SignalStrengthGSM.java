package com.example.signalstrengthgsm;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.BreakIterator;


public class SignalStrengthGSM extends AppCompatActivity {

    TelephonyManager TelephoneManager;
    myPhoneStateListener pslistener;

    private TextView txt1 = findViewById(R.id.textview1);

  public static class myPhoneStateListener extends PhoneStateListener{
      float signalStrengthValue = 0;



      public void onSignalStrengthsChanged(SignalStrength signalStrength){

            this.signalStrengthValue = signalStrength.getGsmSignalStrength();
            this.signalStrengthValue = (2* this.signalStrengthValue) -113;


            System.out.println("#######################");
            System.out.println(String.valueOf(signalStrengthValue) + " dBm");
//            txt1.setText(String.valueOf(signalStrengthValue) + " dBm");
//            System.out.println("txt1: " + txt1);


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