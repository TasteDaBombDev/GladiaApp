package com.zone.app.utils;

public class LocationInfo {

    private double lat;
    private double lng;
    private String name;
    private String path;
    private String atributes;

    public LocationInfo(double lat, double lng, String name, String path, String atributes) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.path = path;
        this.atributes = atributes;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getAtributes() {
        String[] s = atributes.split("\\|");
        return s;
    }

    public void setAtributes(String atributes) {
        this.atributes = atributes;
    }
}
