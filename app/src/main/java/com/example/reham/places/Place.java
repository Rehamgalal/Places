package com.example.reham.places;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reham on 8/22/2018.
 */

public class Place implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };


    private String placeName;
    private String placeDescription;
    private double latitude;
    private double longtude;

    // Constructor
    public Place(
            String placeName,
            String placeDescription,
            double latitude,
            double longtude) {

        this.placeName = placeName;
        this.placeDescription = placeDescription;
        this.latitude = latitude;
        this.longtude = longtude;
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }


    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtude() {
        return longtude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    // Parcelling part
    public Place(Parcel in) {
        this.latitude = in.readDouble();
        this.longtude = in.readDouble();
        this.placeName = in.readString();
        this.placeDescription = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longtude);
        dest.writeString(this.placeName);
        dest.writeString(this.placeDescription);
    }

    @Override
    public String toString() {
        return "Place{" + "latitude='" + latitude + '\'' +
                ", longtude='" + longtude + '\'' +
                ", name='" + placeName + '\'' +
                ", description='" + placeDescription + '\'' +
                '}';
    }
}