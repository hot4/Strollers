package com.example.strollers.strollers.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SouthWest implements Serializable {
    @SerializedName("lat")
    private Double latitude;

    @SerializedName("lng")
    private Double longitude;

    public Double getLat() {
        return latitude;
    }

    public Double getLng() {
        return longitude;
    }
}
