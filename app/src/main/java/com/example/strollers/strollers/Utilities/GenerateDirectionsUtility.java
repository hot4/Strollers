package com.example.strollers.strollers.Utilities;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.example.strollers.strollers.Models.Destination;
import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class GenerateDirectionsUtility {
    private HttpsURLConnection urlConnection;

    /* URL keys */
    private final static String pURL = "https://maps.googleapis.com/maps/api/directions/";
    private static String origin;
    private static String key;
    private static String destination;

    private static void resetParams() {
        origin = "origin=";
        destination = "destination=";
        key = "key=";
    }

    private String getMapsApiDirectionsUrl() {
        //String waypoints = "waypoints=optimize:true|"
        //String params = waypoints + "&" + sensor;
        String output = "json";
        String url = pURL + output + "?" + destination + "&" + origin + "&" + "mode=walking&" +key + "&";
        return url;
    }

    private List<List<HashMap<String, String>>> parseJson(JSONObject data)
    {
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        try {
            jRoutes = data.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat",
                                    Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng",
                                    Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    public List<List<HashMap<String, String>>> getLocations(Context context, Location currentLocation, Destination finalDestination) throws ExecutionException, InterruptedException {

        resetParams();
        origin += Double.toString(currentLocation.getLatitude()) + "," + Double.toString(currentLocation.getLongitude());
        destination += Double.toString(finalDestination.getLat()) + "," + Double.toString(finalDestination.getLng());
        key += context.getString(R.string.google_locations_key);
        String url = getMapsApiDirectionsUrl();
        GetData getData = new GetData();
        String data = getData.execute(url).get();
        List<List<HashMap<String, String>>> result = null;
        try {
            JSONObject jObject = new JSONObject(data);
            result = parseJson(jObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
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
                JSONObject jObject = new JSONObject(result.toString());
                parseJson(jObject);
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

