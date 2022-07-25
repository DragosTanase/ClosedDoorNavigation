package com.example.testsenzorclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Senzor {
    protected File path;
    protected File file;
    protected FileOutputStream f;
    boolean headerCSV = true;

    Senzor(){};

    public void writeCSV(String fileName, String once, String entry) throws FileNotFoundException {
         path = new File("/storage/emulated/0/Download");
         file = new File(path + fileName);
         f = new FileOutputStream(file, true);
        try {

            if(headerCSV==true){
                f.write(once.getBytes());
                f.flush();
                headerCSV=false;
            }

            f.write(entry.getBytes());
            f.flush();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}




