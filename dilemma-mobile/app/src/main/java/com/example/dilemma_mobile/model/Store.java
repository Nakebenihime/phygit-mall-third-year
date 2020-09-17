package com.example.dilemma_mobile.model;

public class Store {
    private String id;
    private String name;
    private String type;
    private double latitude;
    private double longitude;
    private String capteurUUID;

    public Store(String id, String name, String type, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Store(String id, String name, String type, double latitude, double longitude, String UUID) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capteurUUID = UUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCapteurUUID() {
        return capteurUUID;
    }

    public void setCapteurUUID(String capteurUUID) {
        this.capteurUUID = capteurUUID;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
