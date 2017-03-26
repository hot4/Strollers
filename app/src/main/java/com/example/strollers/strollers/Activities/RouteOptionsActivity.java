package com.example.strollers.strollers.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.strollers.strollers.Adapters.RoutesAdapter;
import com.example.strollers.strollers.Constants.Constants;
import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteOptionsActivity extends Activity {

    @BindView(R.id.prompt)
    TextView prompt;
    @BindView(R.id.unit)
    TextView unit;
    @BindView(R.id.amount)
    EditText inputAmount;
    @BindView(R.id.empty_routes)
    LinearLayout emptyRoutes;
    @BindView(R.id.populated_routes)
    RelativeLayout populatedRoutes;
    @BindView(R.id.routes_recycler_view)
    RecyclerView routesRecyclerView;

    private static final String TAG = RouteOptionsActivity.class.getSimpleName();

    private LinkedList<Route> routesList = new LinkedList<>();
    private RecyclerView recyclerView;
    private RoutesAdapter routesAdapter;

    private Location mCurrentLocation;
    private HttpsURLConnection urlConnection;

    final static String pURL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static StringBuilder location = new StringBuilder("location=");
    private static StringBuilder radius = new StringBuilder("radius=");
    private static StringBuilder key = new StringBuilder("key=");

    private static final String rankBy = "rankBy=distance";
    private static final String and = "&";
    private static final String results = "results";

    private static final Double milesMetersRatio = 1609.344;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_options);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            prompt.setText((int) bundle.get(Constants.ROUTE_PROMPT));
            unit.setText((int) bundle.get(Constants.ROUTE_UNIT));
            mCurrentLocation = (Location) bundle.get(Constants.LOCATION);
        }

        recyclerView = routesRecyclerView;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        routesAdapter = new RoutesAdapter(routesList);

        recyclerView.setAdapter(routesAdapter);
        inputAmount.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /* User has completed entering a value */
                if (keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    String amount = inputAmount.getText().toString();
                    Double totalMiles = 0.0;
                    /* Amount is greater than or equal to zero */
                    if (!amount.isEmpty()) {
                        totalMiles = Double.parseDouble(amount);
                    }

                    determineRoutes(totalMiles);
                    displayRoutes();
                    return true;
                }
                return false;
            }
        });
    }

    public LinkedList<Route> determineRoutes(Double totalMiles) {
        routesList.clear();
        if (totalMiles != 0) {
            /* TODO: IMPLEMENT ALGORITHM */
            Double meters = convertMilesToMeters(totalMiles);
            try {
                String data = generateRoutes(meters);
                JSONObject responseOb = new JSONObject(data);
                JSONArray places = responseOb.getJSONArray(results);

                Log.d(TAG, "Captured: " + data);
                Log.d(TAG, "Places: " + places.toString());

            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }
        routesAdapter.notifyDataSetChanged();
        return routesList;
    }

    public void displayRoutes() {
        /* No routes are found */
        if (routesList.isEmpty()) {
            emptyRoutes.setVisibility(View.VISIBLE);
            populatedRoutes.setVisibility(View.GONE);
        } else {
            emptyRoutes.setVisibility(View.GONE);
            populatedRoutes.setVisibility(View.VISIBLE);
        }
    }

    public Double convertMilesToMeters(Double miles) {
        return miles * milesMetersRatio;
    }

    public String generateRoutes(Double meters) throws ExecutionException, InterruptedException {
        StringBuilder portURL = new StringBuilder(pURL);
        location.append(Double.toString(mCurrentLocation.getLatitude())).append(",").append(Double.toString(mCurrentLocation.getLongitude()));
        radius.append(Double.toString(meters));
        key.append(getString(R.string.google_locations_key));
        portURL.append(location).append(and).append(radius).append(and).append(rankBy).append(and).append(key);

        Log.d(TAG, portURL.toString());
        GetData getData = new GetData();
        return getData.execute(portURL.toString()).get();
    }

    public class GetData extends AsyncTask<String, Void, String> {

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