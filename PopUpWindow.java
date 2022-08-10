package com.example.uisimplu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PopUpWindow extends AppCompatDialogFragment{

    private EditText editText;
    protected Canvas mCanvas;
    protected String entry;
    protected String data;
    protected boolean state_save=false;
    String once ="x_acc" + "," + "y_acc" + "," + "z_acc" + "," + "x_gyro" + "," + "y_gyro" + "," + "z_gyro" + "," + "mag_tesla" + "," + "wifi_rssi"+","+"denumire";


    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        builder.setView(view).setTitle("Save").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                entry="";
                storePP(false,once);
                dialogInterface.dismiss();
            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                entry = editText.getText().toString();
                storePP(true,once);
                mCanvas.adPP();

            }
        });
        editText = view.findViewById(R.id.edit_locatie);
        return builder.create();
    }
    public void storePP(boolean state, String once)
    {
        if(state) {
           String  entry=data+getEntry();
            try {
                File path = new File("/storage/emulated/0/Download");
                File file = new File(path + "/PinPoints.csv");
                FileOutputStream f = new FileOutputStream(file, true);
                if (file.length() != 0) {
                    try {
                        f.write(entry.getBytes());
                        f.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        f.write(once.getBytes());
                        f.write(entry.getBytes());
                        f.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public String getEntry()
    {
        return entry;
    }

    public boolean isState() {
        return state_save;
    }

    public void setmCanvas(Canvas mCanvas) {
        this.mCanvas = mCanvas;
    }

    public void setData(String data) {
        this.data = data;
    }
}

