package com.example.dell.myapplication;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class AutoCompleteWrapper {

    //List<AutoCompleteData> autoCompleteData;

    public AutoCompleteWrapper() {}

    public static Collection<AutoCompleteWrapper> fromJson(String s) {
        Type collectionType = new TypeToken<Collection<AutoCompleteWrapper>>(){}.getType();
        Collection<AutoCompleteWrapper> enums = new Gson().fromJson(s, collectionType);
        return enums;
    }
    public String toString() {
        return new Gson().toJson(this);
    }

//}

//class AutoCompleteData {

    @SerializedName("description")
    public String description;

    @SerializedName("id")
    public String id;

    public List<MatchedSubstrings> matched_substrings;

    @SerializedName("place_id")
    public String place_id;

    @SerializedName("reference")
    public String reference;

    public List<Term> terms;

    @SerializedName("types")
    public List<String> types;

}


class MatchedSubstrings {

    @SerializedName("length")
    public int length;

    @SerializedName("offset")
    public int offset;
}

class StructuredFormatting {

    @SerializedName("main_text")
    public String main_text;

    public List<MatchedSubstrings> main_text_matched_substrings;

    @SerializedName("secondary_text")
    public String secondary_text;
}

class Term {

    @SerializedName("offset")
    public int offset;

    @SerializedName("value")
    public String value;
}