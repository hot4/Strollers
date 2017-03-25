package com.example.strollers.strollers.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.strollers.strollers.Constants.Constants;
import com.example.strollers.strollers.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteTypeActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.distance)
    Button distanceButton;
    @BindView(R.id.calorie)
    Button calorieButton;

    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_type);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            mCurrentLocation = (Location) bundle.get(Constants.LOCATION);
        }

        distanceButton.setOnClickListener(this);
        calorieButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), RouteOptionsActivity.class);
        switch (v.getId()) {
            case R.id.distance:
                intent.putExtra(Constants.ROUTE_PROMPT, R.string.distance_prompt);
                intent.putExtra(Constants.ROUTE_UNIT, R.string.distance_unit);
                break;
            case R.id.calorie:
                intent.putExtra(Constants.ROUTE_PROMPT, R.string.calorie_prompt);
                intent.putExtra(Constants.ROUTE_UNIT, R.string.calorie_unit);
                break;
        }

        intent.putExtra(Constants.LOCATION, mCurrentLocation);
        startActivity(intent);
    }
}
