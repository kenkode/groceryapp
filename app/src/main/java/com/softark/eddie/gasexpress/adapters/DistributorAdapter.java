package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softark.eddie.gasexpress.PriceActivity;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.models.Distributor;

import java.util.ArrayList;

/**
 * Created by Eddie on 4/16/2017.
 */

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.ViewHolder> {

    private ArrayList<Distributor> distributors;
    private Context context;
    private LayoutInflater inflater;

    public DistributorAdapter(ArrayList<Distributor> distributors, Context context) {
        this.distributors = distributors;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return distributors.size();
    }

    @Override
    public void onBindViewHolder(DistributorAdapter.ViewHolder holder, int position) {
        Distributor distributor = distributors.get(position);

        holder.distributorName.setText(distributor.getName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PriceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public DistributorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.distributor_list, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView distributorName;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            distributorName = (TextView) itemView.findViewById(R.id.distributor_name);
        }
    }

}
