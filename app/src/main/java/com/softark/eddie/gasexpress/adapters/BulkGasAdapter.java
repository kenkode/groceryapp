package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;

import java.util.ArrayList;

/**
 * Created by Eddie on 5/3/2017.
 */

public class BulkGasAdapter extends RecyclerView.Adapter<BulkGasAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BulkGas> gases;
    private LayoutInflater inflater;

    public BulkGasAdapter(Context context, ArrayList<BulkGas> gases) {
        this.context = context;
        this.gases = gases;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return gases.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public BulkGasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bulk_gas_list, null);
        return new BulkGasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BulkGasAdapter.ViewHolder holder, final int position) {
        final BulkGas gas = gases.get(position);
        final String name = String.valueOf(gas.getSize()).concat(" ").concat(gas.getMetric());
        holder.name.setText(name);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, name.concat(" added to cart"), Toast.LENGTH_LONG).show();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageButton add;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.bulk_text);
            add = (ImageButton) itemView.findViewById(R.id.bulk_gas_more_info);
        }
    }

}
