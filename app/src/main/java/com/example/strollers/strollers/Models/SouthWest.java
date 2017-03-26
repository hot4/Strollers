package com.example.strollers.strollers.Models;

import com.google.gson.annotations.SerializedName;

public class SouthWest {
    @SerializedName("lat")
    private Integer latitude;

    @SerializedName("lng")
    private Integer longitude;

    public Integer getLat() {
        return latitude;
    }

    public Integer getLng() {
        return longitude;
    }
}
