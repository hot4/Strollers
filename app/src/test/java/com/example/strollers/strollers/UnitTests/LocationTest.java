package com.example.strollers.strollers.UnitTests;

import android.content.Context;
import android.location.Location;

import com.example.strollers.strollers.Models.Destination;
import com.example.strollers.strollers.Models.Destinations;
import com.example.strollers.strollers.Utilities.GenerateRoutesUtility;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.test.mock.MockContext;
import android.util.Log;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class LocationTest {
    private Context mContext;
    private Context context;
    private Location sampleLocation = new Location("");
    private Double sampleDistance;
    private static final String results = "results";

    @Before
    public void setup(){
        mContext = new MockContext();
        context = new MockContext();
        sampleLocation = new Location("Test");
        sampleLocation.setLatitude(42.7309885);
        sampleLocation.setLongitude(-73.6820253);
        sampleDistance = 10.0;
    }

    @Test
    public void locationChecker() throws ExecutionException, InterruptedException, JSONException, IOException {
        GenerateRoutesUtility testRoute = new GenerateRoutesUtility();
        assertNotNull(context);
        assertNotNull(sampleLocation);
        assertNotNull(sampleDistance);
        String data = testRoute.getJson(context, sampleLocation, sampleDistance);
        JSONObject responseOb = new JSONObject(data);
        responseOb.getJSONArray(results);

        /* Serialize JSON into Destination and add to list */
        Destinations destinations = Destinations.parseJson(data);
        destinations.initializeDistances(sampleLocation.getLatitude(), sampleLocation.getLongitude());
        Log.d("BLEH", destinations.getDestsList().toString());

        InputStream is = mContext.getAssets().open("distanceCheck.txt");
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