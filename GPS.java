package com.example.uisimplu;

import android.location.Location;
import android.location.LocationManager;

import java.io.FileNotFoundException;

public class GPS extends Senzor{
    protected LocationManager locationManager;
    protected Location location;
    protected int latitude;
    protected int longitude;
}
