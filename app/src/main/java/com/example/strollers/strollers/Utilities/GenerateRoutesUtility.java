package com.example.strollers.strollers.Utilities;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.example.strollers.strollers.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class GenerateRoutesUtility {

    private HttpsURLConnection urlConnection;

    private final static String pURL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static StringBuilder location = new StringBuilder("location=");
    private static StringBuilder radius = new StringBuilder("radius=");
    private static StringBuilder rankBy = new StringBuilder("rankBy=");
    private static StringBuilder type = new StringBuilder("type=");
    private static StringBuilder key = new StringBuilder("key=");

    private static final String and = "&";

    public String getJson(Context context, Location currentLocation, Double meters) throws ExecutionException, InterruptedException {
        StringBuilder portURL = new StringBuilder(pURL);
        location.append(Double.toString(currentLocation.getLatitude())).append(",").append(Double.toString(currentLocation.getLongitude()));
        radius.append(Double.toString(meters));
        rankBy.append(context.getString(R.string.distance_label));
        type.append(context.getString(R.string.cafe));
        key.append(context.getString(R.string.google_locations_key));
        portURL.append(location).append(and).append(radius).append(and).append(rankBy).append(and).append(type).append(and).append(key);

        GetData getData = new GetData();
        return getData.execute(portURL.toString()).get();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }
    }
}
