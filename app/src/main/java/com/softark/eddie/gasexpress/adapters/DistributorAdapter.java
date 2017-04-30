package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.PriceActivity;
import com.softark.eddie.gasexpress.R;

import java.util.ArrayList;

/**
 * Created by Eddie on 4/16/2017.
 */

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.ViewHolder> {

    private String[] sizes;
    private Context context;
    private LayoutInflater inflater;

    public DistributorAdapter(String[] sizes, Context context) {
        this.sizes = sizes;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return sizes.length;
    }

    @Override
    public void onBindViewHolder(DistributorAdapter.ViewHolder holder, int position) {
        final int size = Integer.parseInt(sizes[position]);

        holder.distributorName.setText(String.valueOf(size));

        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PriceActivity.class);
                intent.putExtra(Constants.SIZE, size);
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
        public ImageButton moreInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            distributorName = (TextView) itemView.findViewById(R.id.gas_size);
            moreInfo = (ImageButton) itemView.findViewById(R.id.size_more_info);
        }
    }

}
