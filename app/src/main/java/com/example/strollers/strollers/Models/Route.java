package com.example.strollers.strollers.Models;

import java.util.List;

public class Route {
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private Photos photos;
    private String place_id;
    private String reference;
    private String scope;
    private List<String> types;
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
        return place_id;
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
