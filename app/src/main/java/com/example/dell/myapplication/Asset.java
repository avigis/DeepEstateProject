package com.example.dell.myapplication;

import java.io.Serializable;

public class Asset implements Serializable{

    private String address;
    private String type;
    private String purpose;
    private String price;
    private String imageUrl;

    public Asset(String address, String type, String purpose, String price, String imageUrl) {
        this.address = address;
        this.type = type;
        this.purpose = purpose;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
