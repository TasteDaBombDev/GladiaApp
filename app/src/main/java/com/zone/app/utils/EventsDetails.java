package com.zone.app.utils;

public class EventsDetails {

    private int ID;
    private int order;
    private String name;
    private String date;
    private String ora;
    private String type;
    private String poza;
    private double distance;
    private String location;
    private String muzica;
    private String mbo;

    public EventsDetails(int order, double distance, int ID, String poza, String name, String date, String ora, String type, String muzica, String mbo) {
        this.order = order;
        this.distance = distance;
        this.ID = ID;
        this.poza = poza;
        this.name = name;
        this.date = date;
        this.ora = ora;
        this.type = type;
        this.muzica = muzica;
        this.mbo = mbo;
    }

    public EventsDetails(String name, String ora, String type, String poza, String location) {
        this.name = name;
        this.ora = ora;
        this.type = type;
        this.poza = poza;
        this.location = location;
    }

    public String getMbo() {
        return mbo;
    }

    public void setMbo(String mbo) {
        this.mbo = mbo;
    }

    public String getMuzica() {
        return muzica;
    }

    public void setMuzica(String muzica) {
        this.muzica = muzica;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPoza() {
        return poza;
    }

    public int getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }

    public String getOra() {
        return ora;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public void setPoza(String poza) {
        this.poza = poza;
    }

    public void setType(String type) {
        this.type = type;
    }
}
