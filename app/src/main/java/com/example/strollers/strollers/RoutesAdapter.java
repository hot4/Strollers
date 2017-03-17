package com.example.strollers.strollers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RoutesViewHolder> {

    private LinkedList<Route> routesList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RoutesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.origin_location)
        TextView originLocation;
        @BindView(R.id.destination_location)
        TextView destinationLocation;
        @BindView(R.id.distance_amount)
        TextView distanceAmount;
        //        @BindView

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
    public void onBindViewHolder(RoutesViewHolder holder, int position) {
        Route route = routesList.get(position);
        holder.originLocation.setText(route.getOrigin());
        holder.destinationLocation.setText(route.getDestination());
        holder.distanceAmount.setText(String.format(Locale.US, "%.1f", route.getDistance()));
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }
}
