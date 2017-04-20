package com.example.strollers.strollers.Helpers;

import android.app.Activity;
import android.location.Location;

import com.example.strollers.strollers.Models.Destination;
import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapHelper {

    private static final String TAG = MapHelper.class.getSimpleName();

    private static Marker markMap(GoogleMap map, LatLng mark, String label) {
        /* Mark location on map */
        Marker newMarker = map.addMarker(new MarkerOptions().position(mark).title(label.toUpperCase()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 15.0f));
        return newMarker;
    }

    public static Marker markCurrLocOnMap(GoogleMap map, Marker oldMarker, Location location, String label) {
        /* Remove location from map */
        if (oldMarker != null) {
            oldMarker.remove();
        }

        return markMap(map, new LatLng(location.getLatitude(), location.getLongitude()), label);
    }

    public static Marker markDestOnMap(GoogleMap map, Marker oldMarker, Destination dest, String label) {
        /* Remove destination from map */
        if (oldMarker != null) {
            oldMarker.remove();
        }

        return markMap(map, new LatLng(dest.getLat(), dest.getLng()), label);
    }

    public static PolylineOptions drawRoute(Activity activty, Route route) {
////        List<List<HashMap<String, String>>> routes
        List<LatLng> points = new ArrayList<>();
        PolylineOptions polyLineOptions = new PolylineOptions();
//
//        // traversing through routes
//        for (int i = 0; i < routes.size(); i++) {
//            points = new ArrayList<LatLng>();
//            polyLineOptions = new PolylineOptions();
//            List<HashMap<String, String>> path = routes.get(i);
//
//            for (int j = 0; j < path.size(); j++) {
//                HashMap<String, String> point = path.get(j);
//
//                double lat = Double.parseDouble(point.get("lat"));
//                double lng = Double.parseDouble(point.get("lng"));
//                LatLng position = new LatLng(lat, lng);
        points.add(route.getOriginLatLng());
        points.add(route.getDestLatLng());
//                points.add(position);
//            }
//
            polyLineOptions.addAll(points);
            polyLineOptions.width(5);
            polyLineOptions.color(activty.getColor(R.color.blue));
//        }
        return polyLineOptions;
    }
}
