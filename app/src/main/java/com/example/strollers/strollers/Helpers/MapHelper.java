package com.example.strollers.strollers.Helpers;

import android.location.Location;

import com.example.strollers.strollers.Models.Destination;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHelper {

    private static Marker markMap(GoogleMap map, LatLng mark, String label) {
        Marker newMarker = map.addMarker(new MarkerOptions().position(mark).title(label.toUpperCase()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 15.0f));
        return newMarker;
    }

    public static Marker markCurrLocOnMap(GoogleMap map, Marker oldMarker, Location location, String label) {
        if (oldMarker != null) {
            oldMarker.remove();
        }

        return markMap(map, new LatLng(location.getLatitude(), location.getLongitude()), label);
    }

    public static Marker markDestOnMap(GoogleMap map, Marker oldMarker, Destination dest, String label) {
        if (oldMarker != null) {
            oldMarker.remove();
        }

        return markMap(map, new LatLng(dest.getLat(), dest.getLng()), label);
    }
}
