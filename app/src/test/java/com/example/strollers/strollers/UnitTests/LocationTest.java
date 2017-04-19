package com.example.strollers.strollers.UnitTests;

import android.content.Context;
import android.location.Location;
import android.test.mock.MockContext;
import android.util.Log;

import com.example.strollers.strollers.BuildConfig;
import com.example.strollers.strollers.Helpers.RouteHelper;
import com.example.strollers.strollers.Models.Destination;
import com.example.strollers.strollers.Models.Destinations;
import com.example.strollers.strollers.R;
import com.example.strollers.strollers.Utilities.GenerateRoutesUtility;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LocationTest {

    private Location sampleLocation;
    private Double sampleDistance;
    private static final String results = "results";

    @Before
    public void setup(){
        sampleLocation = new Location("Test");
        sampleLocation.setLatitude(42.7309885);
        sampleLocation.setLongitude(-73.6820253);
        sampleDistance = RouteHelper.convertMilesToMeters(10.0);
    }

    @Test
    public void locationChecker() throws ExecutionException, InterruptedException, JSONException, IOException {
        GenerateRoutesUtility testRoute = new GenerateRoutesUtility();
        String data = testRoute.getJson("AIzaSyCnF4sg6MaCkXXYs8LUNcf8hRWI5eJ4XpI", "distance", sampleLocation, sampleDistance);
        JSONObject responseOb = new JSONObject(data);
        responseOb.getJSONArray(results);

        /* Serialize JSON into Destination and add to list */
        Destinations destinations = Destinations.parseJson(data);
        destinations.initializeDistances(sampleLocation.getLatitude(), sampleLocation.getLongitude());

        String file = "/distanceCheck.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        List<Destination> destsList = destinations.getDestsList();
        for (int i = 0; i < destsList.size(); i++) {
            line = reader.readLine();
            assertEquals(line,destsList.get(i).getName());
            line = reader.readLine();
            assertEquals(line,destsList.get(i).getDistance().toString());
        }
    }
}