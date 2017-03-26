package com.example.strollers.strollers.Models;

import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    private Location location;

    @SerializedName("viewport")
    private ViewPort viewPort;

    public Location getLocation() {
        return location;
    }

    public ViewPort viewPort() {
        return viewPort;
    }
}
