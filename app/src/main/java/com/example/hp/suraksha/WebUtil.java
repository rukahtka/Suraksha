package com.example.hp.suraksha;

/**
 * Created by hp on 21/10/17.
 */
import android.location.Location;
import java.util.ArrayList;

public class WebUtil {
    private static double curLat = 1000.0d;
    private static double curLog = 1000.0d;
    private static Location location;

    public static boolean isLocationSet() {
        if (curLat == 1000.0d && curLog == 1000.0d) {
            return false;
        }
        return true;
    }

    public static void setCurrentLocation(double lat, double log) {
        curLat = lat;
        curLog = log;
    }

    public static double getCurLat() {
        return curLat;
    }

    public static double getCurLog() {
        return curLog;
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        location = location;
    }
}