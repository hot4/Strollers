package com.example.strollers.strollers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.strollers.strollers.Activities.MainActivity;
import com.example.strollers.strollers.Constants.Constants;
import com.example.strollers.strollers.Helpers.RouteHelper;
import com.example.strollers.strollers.Helpers.SharedPreferencesHelper;
import com.example.strollers.strollers.Models.Destination;
import com.example.strollers.strollers.R;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.RoutesViewHolder> {

    private Activity mActivity;
    private Context mContext;
    private List<Destination> destsList;

    public static class RoutesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.origin_location)
        TextView originLocation;
        @BindView(R.id.destination_location)
        TextView destinationLocation;
        @BindView(R.id.distance_amount)
        TextView distanceAmount;

        // each data item is just a string in this case
        public RoutesViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public DestinationsAdapter(Activity activity, List<Destination> destsList) {
        this.mActivity = activity;
        this.destsList = destsList;
    }

    @Override
    public RoutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RoutesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RoutesViewHolder holder, final int position) {
        final Destination destination = destsList.get(position);

        SharedPreferences sharedPrefs = mActivity.getPreferences(Context.MODE_PRIVATE);
        Double currLat = SharedPreferencesHelper.getDouble(sharedPrefs, Constants.LATITUDE);
        Double currLng = SharedPreferencesHelper.getDouble(sharedPrefs, Constants.LONGITUDE);

        Double distance = RouteHelper.distance(currLat, currLng, destination.getLat(), destination.getLng());
        StringBuilder total = new StringBuilder(String.format(Locale.US, "%1$,.2f", distance));
        total.append(" ");
        total.append(mActivity.getString(R.string.distance_unit));

        holder.destinationLocation.setText(destination.getName());
        holder.distanceAmount.setText(total.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra(Constants.DESTINATION, destination);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return destsList.size();
    }
}
