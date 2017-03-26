package com.example.strollers.strollers.Models;

public class Route {
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private Photos photos;

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
