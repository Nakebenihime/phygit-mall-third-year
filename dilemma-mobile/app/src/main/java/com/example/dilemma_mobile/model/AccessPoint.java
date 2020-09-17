package com.example.dilemma_mobile.model;

public class AccessPoint {
    private String mac;
    private double x;
    private double y;
    private int floor;
    private double distanceFromUser;

    public AccessPoint(String mac, double x, double y, int floor) {
        this.mac = mac;
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public String getMac() {
        return mac;
    }

    public void setSsid(String mac) {
        this.mac = mac;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }
}
