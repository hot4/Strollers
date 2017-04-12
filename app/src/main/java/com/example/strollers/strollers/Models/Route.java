package com.example.strollers.strollers.Models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Route {

    public Location currLoc;
    public Destination destLoc;

    public Route(Location currLoc, Destination destLoc) {
        this.currLoc = currLoc;
        this.destLoc = destLoc;
    }

    public Location getOrigin() {
        return currLoc;
    }

    public Destination getDestination() {
        return destLoc;
    }

    public LatLng getOriginLatLng() {
        return new LatLng(currLoc.getLatitude(), currLoc.getLongitude());
    }

    public LatLng getDestLatLng() {
        return new LatLng(destLoc.getLat(), destLoc.getLng());
    }
}
