import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class signalStrengthGSM extends AppCompatActivity {

    TextView txt1;

//    @SuppressLint("ServiceCast")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        txt1 = (TextView) findViewById(R.id.textview1);
//
//        try{
//            pslistener = new MainActivity.myPhoneStateListener();
//            TelephoneManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            TelephoneManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    signalStrengthGSM(){

    }

    TelephonyManager TelephoneManager;
    MainActivity.myPhoneStateListener pslistener;
    int SignalStrength = 0;

    class myPhoneStateListener extends PhoneStateListener{
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength){
            super.onSignalStrengthsChanged(signalStrength);
            SignalStrength = signalStrength.getGsmSignalStrength();
            SignalStrength = (2* SignalStrength) -113;
            txt1.setText(String.valueOf(SignalStrength + "dBm"));

            String entry = "\n" + String.format("%.3f dBm", SignalStrength);


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id==R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}
