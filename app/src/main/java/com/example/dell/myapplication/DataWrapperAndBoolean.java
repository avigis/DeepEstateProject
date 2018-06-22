package com.example.dell.myapplication;

import java.io.Serializable;

public class DataWrapperAndBoolean implements Serializable {

    DataWrapper dataWrapper;
    DataWrapper filteredDataWrapper;
    boolean filtered;

    public DataWrapperAndBoolean(DataWrapper dataWrapper, DataWrapper filteredDataWrapper , boolean filtered) {
        this.dataWrapper = dataWrapper;
        this.filteredDataWrapper = filteredDataWrapper;
        this.filtered = filtered;
    }
}
