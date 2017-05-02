package com.example.strollers.strollers.Models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.List;

public class Route {

    public List<LatLng> points;
//    public LatLng currLoc;
    public Destination destLoc;
    public Double distance;

//    public Route(LatLng currLoc, Destination destLoc) {
//        this.currLoc = currLoc;
//        this.destLoc = destLoc;
//    }
//
//    public Route(Double dist, LatLng currLoc, Destination destLoc) {
//        this.currLoc = currLoc;
//        this.destLoc = destLoc;
//        this.distance = dist;
//    }

//    public LatLng getOrigin() {
//        return currLoc;
//    }

    public void addPoint(LatLng point)
    {
        points.add(point);
    }

    public Destination getDestination() {
        return destLoc;
    }

    public List<LatLng> getPoints() {return points;}
//    public LatLng getOriginLatLng() {
//        return new LatLng(currLoc.latitude, currLoc.longitude);
//    }

    public LatLng getDestLatLng() {
        return new LatLng(destLoc.getLat(), destLoc.getLng());
    }
}
