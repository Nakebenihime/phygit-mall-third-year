package com.example.dilemma_mobile.model;

public class Location {

    private double x;
    private double y;
    private int floor;

    public Location(double x, double y, int floor) {
        this.x = x;
        this.y = y;
        this.floor = floor;
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
}
