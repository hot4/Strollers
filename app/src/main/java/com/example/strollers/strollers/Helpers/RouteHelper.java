package com.example.strollers.strollers.Helpers;

import android.location.Location;

public class RouteHelper {

    /* Constant ratio */
    private static final Double milesMetersRatio = 1609.344;

    public static Double convertMilesToMeters(Double miles) {
        return miles * milesMetersRatio;
    }

    public static Double convertMetersToMiles(Double meters) {
        return meters / milesMetersRatio;
    }

    public static Double distance(Double lat1, Double lon1, Double lat2, Double lon2) {
        /* Calculate distance between coordinates */
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);

        return convertMetersToMiles((double) results[0]);
    }
}
