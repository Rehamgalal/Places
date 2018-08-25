package com.example.reham.places;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by reham on 8/25/2018.
 */

public class WidgetDataOnSharedPreferences {

    private WidgetDataOnSharedPreferences() {

    }

    public static void saveWidgetDataOnSharedPreferences(Context context, int id, String name, String latitude, String longtude) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("id", id);
        editor.putString("placename", name);
        editor.putString("latitude", latitude);
        editor.putString("longtude", longtude);
        editor.apply();
    }


    public static String[] getWidgetDataFromSharedPreferences(Context context) {
        String[] widgetData = new String[4];

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        int id = sharedPref.getInt("id", 0);
        String name = sharedPref.getString("placename", "name");
        String latitude = sharedPref.getString("latitde", "0.0");
        String longtude = sharedPref.getString("longtude", "0.0");
        widgetData[0] = String.valueOf(id);
        widgetData[1] = name;
        widgetData[2] = latitude;
        widgetData[3] = longtude;

        return widgetData;
    }
}
