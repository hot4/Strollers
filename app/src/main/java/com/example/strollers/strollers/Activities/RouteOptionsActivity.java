package com.example.strollers.strollers.Activities;

import android.app.Activity;
import android.content.Intent;
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
import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.R;

import java.util.LinkedList;

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

    private LinkedList<Route> routesList = new LinkedList<>();
    private RecyclerView recyclerView;
    private RoutesAdapter routesAdapter;

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
                    Integer totalAmount = 0;
                    /* Amount is greater than or equal to zero */
                    if (!amount.isEmpty()) {
                        totalAmount = Integer.parseInt(amount);
                    }
                    determineRoutes(totalAmount);
                    displayRoutes();
                    return true;
                }
                return false;
            }
        });
    }

    public LinkedList<Route> determineRoutes(Integer totalAmount) {
        routesList.clear();
        if (totalAmount != 0) {
            /* TODO: IMPLEMENT ALGORITHM */
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
}
