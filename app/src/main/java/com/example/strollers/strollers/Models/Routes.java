package com.example.strollers.strollers.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Routes {

    @SerializedName("results")
    private List<Route> routesList;

    public Routes() {
        routesList = new ArrayList<>();
    }

    public List getRoutesList() {
        return routesList;
    }

    public static Routes parseJson(String response) {
        /* Serialize JSON into Destination */
        Gson gson = new GsonBuilder().create();
        Routes routesResponse = gson.fromJson(response, Routes.class);
        return routesResponse;
    }
}
