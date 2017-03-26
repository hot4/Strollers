package com.example.strollers.strollers.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photos {

    @SerializedName("height")
    private Double height;

    @SerializedName("html_attributions")
    private List<String> htmlAttributions;

    @SerializedName("photo_reference")
    private String photoReference;

    @SerializedName("width")
    private Double width;

    public Double getHeight() {
        return height;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public Double getWidth() {
        return width;
    }
}
