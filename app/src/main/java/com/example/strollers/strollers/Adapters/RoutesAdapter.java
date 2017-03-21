package com.example.strollers.strollers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.R;

import java.util.LinkedList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RoutesViewHolder> {

    private LinkedList<Route> routesList;

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

    public RoutesAdapter(LinkedList<Route> routesList) {
        this.routesList = routesList;
    }

    @Override
    public RoutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RoutesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RoutesViewHolder holder, final int position) {
        Route route = routesList.get(position);

        holder.originLocation.setText(route.getOrigin());
        holder.destinationLocation.setText(route.getDestination());
        holder.distanceAmount.setText(String.format(Locale.US, "%.1f", route.getDistance()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }
}
