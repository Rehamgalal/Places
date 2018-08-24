package com.example.reham.places;

import android.net.Uri;
import android.provider.BaseColumns;


public class PlacesContract {

    public static final String Authority = "com.example.reham.places";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + Authority);
    public static final String path = "locations";

    public static class Entry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(path).build();
        public static String tableName = "locations";
        public static String placeName = "PlaceName";
        public static String placeDescription = "PlaceDescription";
        public static String latitude = "Latitude";
        public static String longtude = "Longtude";

    }
}
