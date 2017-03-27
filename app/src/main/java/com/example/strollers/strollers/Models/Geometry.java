package com.example.strollers.strollers.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {
    @SerializedName("location")
    private Position position;

    @SerializedName("viewport")
    private ViewPort viewPort;

    public Position getPosition() {
        return position;
    }

    public ViewPort viewPort() {
        return viewPort;
    }
}
