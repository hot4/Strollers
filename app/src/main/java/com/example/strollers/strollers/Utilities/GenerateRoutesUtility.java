package com.example.strollers.strollers.Utilities;

import android.location.Location;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class GenerateRoutesUtility {

    private HttpsURLConnection urlConnection;

    /* URL keys */
    private final static String pURL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static StringBuilder location;
    private static StringBuilder radius;
    private static StringBuilder rankBy;
    private static StringBuilder key;

    private static final String and = "&";

    private static void resetParams() {
        location = new StringBuilder("location=");
        radius = new StringBuilder("radius=");
        rankBy = new StringBuilder("rankBy=");
        key = new StringBuilder("key=");
    }

    public String getJson(String apiKey, String label, Location currentLocation, Double meters) throws ExecutionException, InterruptedException {
        resetParams();

        /* Build URL */
        StringBuilder portURL = new StringBuilder(pURL);
        location.append(Double.toString(currentLocation.getLatitude())).append(",").append(Double.toString(currentLocation.getLongitude()));
        radius.append(Double.toString(meters));
        rankBy.append(label);
        key.append(apiKey);
        portURL.append(location).append(and).append(radius).append(and).append(rankBy).append(and).append(key);

        /* Get data from URL */
        GetData getData = new GetData();
        return getData.execute(portURL.toString()).get();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                /* Open up URL connection and retrieve data */
                URL url = new URL(params[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }
    }
}
