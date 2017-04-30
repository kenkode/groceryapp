package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.models.Location;

import java.util.ArrayList;

/**
 * Created by Eddie on 4/30/2017.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Location> locationArrayAdapter;
    private Context context;
    private LayoutInflater inflater;
    private MyLocationData data;

    public LocationAdapter(Context context, ArrayList<Location> locationArrayAdapter) {
        this.context = context;
        this.locationArrayAdapter = locationArrayAdapter;
        inflater = LayoutInflater.from(context);
        data = new MyLocationData(context);
    }

    @Override
    public int getItemCount() {
        return locationArrayAdapter.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_location, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Location location = locationArrayAdapter.get(position);
        holder.locationName.setText(location.getAddress());
        holder.locationDesc.setText(location.getAddress());
        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationArrayAdapter.remove(position);
                data.disableLocation(location.getId());
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, locationArrayAdapter.size());
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView locationName, locationDesc;
        public ImageButton closeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            locationName = (TextView) itemView.findViewById(R.id.location_name);
            locationDesc = (TextView) itemView.findViewById(R.id.location_description);
            closeButton = (ImageButton) itemView.findViewById(R.id.cancel_button);
        }
    }

}
