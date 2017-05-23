package com.softark.eddie.gasexpress.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.models.Location;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private final ArrayList<Location> locationArrayAdapter;
    private final Context context;
    private final LayoutInflater inflater;
    private final MyLocationData data;

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
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_location, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Location location = locationArrayAdapter.get(position);
        holder.locationName.setText(location.getAddress());
        holder.locationDesc.setText(location.getAddress());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView locationName;
        public final TextView locationDesc;
        public final ImageButton closeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            locationName = (TextView) itemView.findViewById(R.id.location_name);
            locationDesc = (TextView) itemView.findViewById(R.id.location_description);
            closeButton = (ImageButton) itemView.findViewById(R.id.cancel_button);
            closeButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Location location = locationArrayAdapter.get(getAdapterPosition());
            final Dialog dialog = new Dialog(context);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.remove_location_dialog);
            dialog.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.disableLocation(location.getId(), closeButton);
                    locationArrayAdapter.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), locationArrayAdapter.size());
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

}
