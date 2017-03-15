package com.example.strollers.strollers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RouteOptionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_options);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            TextView tv = (TextView) findViewById(R.id.prompt);
            tv.setText((int) bundle.get(Constants.UNIT_PROMPT));
            tv = (TextView) findViewById(R.id.unit);
            tv.setText((int) bundle.get(Constants.UNIT));
        }

    }
}
