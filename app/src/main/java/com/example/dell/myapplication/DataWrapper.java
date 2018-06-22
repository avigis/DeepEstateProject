package com.example.dell.myapplication;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataWrapper implements Serializable {


    public List<Data> data;
    public Meta meta;

    public static DataWrapper fromJson(String s) {
        return new Gson().fromJson(s, DataWrapper.class);
    }

    public static DataWrapper copyDataWrapper(DataWrapper dt) {
        DataWrapper newDt = new DataWrapper();
        newDt.data = new ArrayList<Data>();
        newDt.meta = Meta.copyMeta(dt.meta);
        for(int i = 0; i < dt.data.size(); i++) {
            newDt.data.add(Data.copyData(dt.data.get(i)));
        }
        return newDt;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


    public DataWrapper() {
    }

    public String toString() {
        return new Gson().toJson(this);
    }

}

class Meta implements Serializable {
    public boolean hasNextPage;
    public List<List<List<List<Double>>>> cityPolygon;

    public Meta(){}

    public static Meta copyMeta(Meta meta) {
        Meta newMeta = new Meta();
//        newMeta.cityPolygon = new ArrayList<Meta>(meta.cityPolygon);
        if(meta != null && meta.cityPolygon != null) {
            for (int i = 0; i < meta.cityPolygon.size(); i++) {
                for (int j = 0; j < meta.cityPolygon.size(); j++) {
                    for (int k = 0; k < meta.cityPolygon.size(); k++) {
                        for (int m = 0; m < meta.cityPolygon.size(); m++) {
                            newMeta.cityPolygon.get(i).get(j).get(k).add(meta.cityPolygon.get(i).get(j).get(k).get(m));
                        }
                    }
                }
            }
            newMeta.hasNextPage = meta.hasNextPage;
        }
        return newMeta;
    }
}


class Data implements Serializable {

    public additionalInfo additional_info;

    public Address address;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("id")
    public String id;

    @SerializedName("is_promoted")
    public boolean is_promoted;

    @SerializedName("price")
    public long price;

    @SerializedName("property_type")
    public String property_type;

    @SerializedName("search_date")
    public String search_date;

    @SerializedName("search_option")
    public String search_option;

    @SerializedName("thumbnail")
    public String thumbnail;

    public Data() {}

    public static Data copyData(Data data) {
        Data newData = new Data();
        newData.created_at = data.created_at;
        newData.id = data.id;
        newData.is_promoted = data.is_promoted;
        newData.price = data.price;
        newData.property_type = data.property_type;
        newData.search_date = data.search_date;
        newData.search_option = data.search_option;
        newData.thumbnail = data.thumbnail;
        newData.additional_info = additionalInfo.copyAdditionalInfo(data.additional_info);
        newData.address = Address.copyAddress(data.address);

        return newData;
    }

    public boolean equal(Data data) {
        if(data.thumbnail != null && !data.thumbnail.equals(this.thumbnail))
            return false;
        if(data.search_option != null && !data.search_option.equals(this.search_option))
            return false;
        if(data.property_type != null && !data.property_type.equals(this.property_type))
            return false;
        if(data.is_promoted != this.is_promoted)
            return false;
        if(data.id != null && !data.id.equals(this.id))
            return false;
        if(data.price != this.price)
            return false;
        if(data.search_date != null && !data.search_date.equals(this.search_date))
            return false;
        return true;
    }

}

class additionalInfo implements Serializable {

    public Area area;

    @SerializedName("bathrooms")
    public int bathrooms;

    public Floor floor;

    public Parking parking;

    @SerializedName("rooms")
    public double rooms;

    public additionalInfo() {}

    public static additionalInfo copyAdditionalInfo(additionalInfo ai) {
        additionalInfo newAi = new additionalInfo();
        newAi.rooms = ai.rooms;
        newAi.bathrooms = ai.bathrooms;
        newAi.area = Area.copyArea(ai.area);
        newAi.floor = Floor.copyFloor(ai.floor);
        newAi.parking = Parking.copyParking(ai.parking);
        return newAi;
    }

}

class Address implements Serializable {

    public En en;
    public Fr fr;

    @SerializedName("google_place_id")
    public String google_place_id;
    public He he;
    public Location location;

    @SerializedName("place_id")
    public String place_id;
    public Ru ru;

    @SerializedName("tags")
    public List<String> tags;

    public Address(){}

    public static Address copyAddress(Address address) {
        Address newAddress = new Address();
        newAddress.place_id = address.place_id;
        newAddress.en = En.copyEn(address.en);
        newAddress.he = He.copyHe(address.he);
        newAddress.fr = Fr.copyFr(address.fr);
        newAddress.ru = Ru.copyRu(address.ru);
        newAddress.location = Location.copyLocation(address.location);
        return newAddress;
    }
}

class Area implements Serializable {

    @SerializedName("base")
    public int base;

    @SerializedName("field")
    public int field;

    @SerializedName("garden")
    public int garden;

    public Area(){}

    public static Area copyArea(Area area) {
        Area newArea = new Area();
        newArea.base = area.base;
        newArea.field = area.field;
        newArea.garden = area.garden;
        return newArea;
    }

}

class Floor implements Serializable {

    @SerializedName("on_the")
    public int on_the;

    @SerializedName("out_of")
    public int out_of;

    public Floor(){}

    public static Floor copyFloor(Floor floor) {
        Floor newFloor = new Floor();
        newFloor.on_the = floor.on_the;
        newFloor.out_of = floor.out_of;
        return newFloor;
    }

}

class Parking implements Serializable {

    @SerializedName("aboveground")
    public String aboveground;

    @SerializedName("underground")
    public String underground;

    public Parking(){
        aboveground = new String();
        underground = new String();
    }

    public static Parking copyParking(Parking parking) {
        Parking newParking = new Parking();
        if(parking != null) {
            newParking.aboveground = parking.aboveground;
            newParking.underground = parking.underground;

            return newParking;
        }
        return null;
    }
}

class En implements Serializable {

    @SerializedName("city_name")
    public String city_name;

    @SerializedName("house_number")
    public String house_number;

    @SerializedName("neighborhood")
    public String neighborhood;

    @SerializedName("street_name")
    public String street_name;

    public En(){}

    public static En copyEn(En en) {
        En newEn = new En();
        if (en != null) {
            newEn.city_name = en.city_name;
            newEn.house_number = en.house_number;
            newEn.neighborhood = en.neighborhood;
            newEn.street_name = en.street_name;
            return newEn;
        }
        return null;
    }

}

class He implements Serializable {

    @SerializedName("city_name")
    public String city_name;

    @SerializedName("house_number")
    public String house_number;

    @SerializedName("neighborhood")
    public String neighborhood;

    @SerializedName("street_name")
    public String street_name;

    public He(){}

    public static He copyHe(He he) {
        He newHe = new He();
        if (he != null) {
            newHe.city_name = he.city_name;
            newHe.house_number = he.house_number;
            newHe.neighborhood = he.neighborhood;
            newHe.street_name = he.street_name;
            return newHe;
        }
        return null;
    }
}

class Fr implements Serializable {

    @SerializedName("city_name")
    public String city_name;

    @SerializedName("house_number")
    public String house_number;

    @SerializedName("neighborhood")
    public String neighborhood;

    @SerializedName("street_name")
    public String street_name;

    public Fr(){}

    public static Fr copyFr(Fr fr) {
        Fr newFr = new Fr();
        if (fr != null) {
            newFr.city_name = fr.city_name;
            newFr.house_number = fr.house_number;
            newFr.neighborhood = fr.neighborhood;
            newFr.street_name = fr.street_name;
            return newFr;
        }
        return null;
    }
}

class Ru implements Serializable {

    @SerializedName("city_name")
    public String city_name;

    @SerializedName("house_number")
    public String house_number;

    @SerializedName("neighborhood")
    public String neighborhood;

    @SerializedName("street_name")
    public String street_name;

    public Ru(){}

    public static Ru copyRu(Ru ru) {
        Ru newRu = new Ru();
        if (ru != null) {
            newRu.city_name = ru.city_name;
            newRu.house_number = ru.house_number;
            newRu.neighborhood = ru.neighborhood;
            newRu.street_name = ru.street_name;
            return newRu;
        }
        return null;
    }

}

class Location implements Serializable {

    @SerializedName("lat")
    public double lat;

    @SerializedName("lon")
    public double lon;

    public Location(){}

    public static Location copyLocation(Location location) {
        Location newLocation = new Location();
        newLocation.lat = location.lat;
        newLocation.lon = location.lon;
        return newLocation;
    }
}





