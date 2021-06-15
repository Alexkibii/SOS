package com.xelacm.sos.models;

public class Location {

    double Longitude;
    double Latitude;
    String Provider;

    public Location() {
    }

    public Location(double longitude, double latitude, String provider) {
        Longitude = longitude;
        Latitude = latitude;
        Provider = provider;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        Provider = provider;
    }
}
