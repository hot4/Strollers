package com.example.strollers.strollers.Helpers;

import android.content.Context;
import android.location.Location;

import com.example.strollers.strollers.Models.Destination;
import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.Utilities.GenerateDirectionsUtility;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class RouteHelper {

    /* Constant ratio */
    private static final Double milesMetersRatio = 1609.344;

    public static Double convertMilesToMeters(Double miles) {
        return miles * milesMetersRatio;
    }

    public static Double convertMetersToMiles(Double meters) {
        return meters / milesMetersRatio;
    }


    public static Double distance(Context context, Location loc, Destination des) {
        /* Calculate distance between coordinates */
        float[] results = new float[1];
        List<Route> routes = null;
        double total = 0;
        try {
            //call generate directions utility and loop thorugh each point to get the distance
            GenerateDirectionsUtility generateDirectionsUtility = new GenerateDirectionsUtility();
            routes = generateDirectionsUtility.getLocations(context, loc, des);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for(int i=0; i< routes.size(); i++) {
            total += routes.get(i).distance;
        }
        return convertMetersToMiles(total);
    }
}
