package com.example.dell.myapplication;

import java.io.Serializable;

public class Filters implements Serializable {

    private String purpose;
    private String type;
    private int min_price;
    private int max_price;
    private int rooms;
    private int min_floor;
    private int max_floor;
    private boolean parking;

    public Filters(String purpose, String type, int min_price, int max_price, int rooms, int min_floor, int max_floor, boolean parking) {
        this.purpose = purpose;
        this.type = type;
        this.min_price = min_price;
        this.max_price = max_price;
        this.rooms = rooms;
        this.min_floor = min_floor;
        this.max_floor = max_floor;
        this.parking = parking;
    }

    public Filters() {}

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMin_price() {
        return min_price;
    }

    public void setMin_price(int min_price) {
        this.min_price = min_price;
    }

    public int getMax_price() {
        return max_price;
    }

    public void setMax_price(int max_price) {
        this.max_price = max_price;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getMin_floor() {
        return min_floor;
    }

    public void setMin_floor(int min_floor) {
        this.min_floor = min_floor;
    }

    public int getMax_floor() {
        return max_floor;
    }

    public void setMax_floor(int max_floor) {
        this.max_floor = max_floor;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }
}
