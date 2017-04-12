package com.example.strollers.strollers.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.strollers.strollers.Adapters.RoutesAdapter;
import com.example.strollers.strollers.Constants.Constants;
import com.example.strollers.strollers.Helpers.RouteHelper;
import com.example.strollers.strollers.Helpers.SharedPreferencesHelper;
import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.Models.Routes;
import com.example.strollers.strollers.R;
import com.example.strollers.strollers.Utilities.GenerateRoutesUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    private List<Route> routesList = new ArrayList<>();
    private RoutesAdapter routesAdapter;

    private Location mCurrentLocation;

    private static final String results = "results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_options);
        ButterKnife.bind(this);

        /* Set text for display and get current location */
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                prompt.setText((int) bundle.get(Constants.ROUTE_PROMPT));
                unit.setText((int) bundle.get(Constants.ROUTE_UNIT));
                mCurrentLocation = (Location) bundle.get(Constants.LOCATION);
            }
        }

        /* Set up adapter to display destinations */
        RecyclerView recyclerView = routesRecyclerView;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        routesAdapter = new RoutesAdapter(this, routesList);

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

    public List<Route> determineRoutes(Double totalMiles) {
        /* Clear previous destination list */
        routesList.clear();
        if (totalMiles != 0) {
            Double radius = RouteHelper.convertMilesToMeters(totalMiles);
            try {
                /* URL request to get destinations */
                GenerateRoutesUtility generateRoutes = new GenerateRoutesUtility();
                String data = generateRoutes.getJson(getApplicationContext(), mCurrentLocation, radius);
                JSONObject responseOb = new JSONObject(data);
                responseOb.getJSONArray(results);

                /* Serialize JSON into Destination and add to list */
                Routes routes = Routes.parseJson(data);
                routesList.addAll(routes.getRoutesList());
            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }

        /* Save latitude and longitude coordinates into shared preferences */
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        SharedPreferencesHelper.putDouble(editor, Constants.LATITUDE, mCurrentLocation.getLatitude());
        SharedPreferencesHelper.putDouble(editor, Constants.LONGITUDE, mCurrentLocation.getLongitude());
        editor.apply();

        /* Update adapter with new destinations list */
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
}