package com.example.strollers.strollers.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("icon")
    private String icon;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("photos")
    private Photos photos;

    @SerializedName("place_id")
    private String placeid;

    @SerializedName("reference")
    private String reference;

    @SerializedName("scope")
    private String scope;

    @SerializedName("types")
    private List<String> types;

    @SerializedName("vicinity")
    private String vicinity;

    private String origin;
    private String destination;
    private Double distance;

    public Route(String origin, String destination, Double distance) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Photos getPhotos() {
        return photos;
    }

    public String getPlaceID() {
        return placeid;
    }

    public String getReference() {
        return reference;
    }

    public String getScope() {
        return scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Double getDistance() {
        return distance;
    }
}
