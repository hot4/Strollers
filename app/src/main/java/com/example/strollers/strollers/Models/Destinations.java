package com.example.strollers.strollers.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Destinations {

    @SerializedName("results")
    private List<Destination> destsList;

    public Destinations() {
        destsList = new ArrayList<>();
    }

    public List getDestsList() {
        return destsList;
    }

    public static Destinations parseJson(String response) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, Destinations.class);
    }
}
