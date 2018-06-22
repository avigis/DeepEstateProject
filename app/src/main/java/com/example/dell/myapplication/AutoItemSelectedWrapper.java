package com.example.dell.myapplication;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoItemSelectedWrapper {

    List<Results> results;

    @SerializedName("status")
    String status;

    public static AutoItemSelectedWrapper fromJson(String s) {
        return new Gson().fromJson(s, AutoItemSelectedWrapper.class);
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}


class Results {

    List<AddressComponent> address_components;

    @SerializedName("formatted_address")
    String formatted_address;

    Geometry geometry;

    @SerializedName("place_id")
    String place_id;

    @SerializedName("types")
    List<String> types;

}

class AddressComponent {

    @SerializedName("long_name")
    String long_name;

    @SerializedName("short_name")
    String short_name;

    @SerializedName("types")
    List<String> types;
}

class Geometry {

    NorthSouth bounds;
    LatLng location;

    @SerializedName("location_type")
    String location_type;

    NorthSouth viewport;


}

class NorthSouth {

    LatLng northeast;

    LatLng southwest;
}

class LatLng {

    @SerializedName("lat")
    double lat;

    @SerializedName("lng")
    double lng;
}