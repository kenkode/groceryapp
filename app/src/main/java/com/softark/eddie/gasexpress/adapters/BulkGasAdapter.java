package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.activities.GECartActivity;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.BulkGas;

import java.util.ArrayList;

public class BulkGasAdapter extends RecyclerView.Adapter<BulkGasAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<BulkGas> gases;
    private final LayoutInflater inflater;

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
    public BulkGasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bulk_gas_list, null);
        return new BulkGasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BulkGasAdapter.ViewHolder holder, int position) {
        BulkGas gas = gases.get(position);
        String metric;
        if(gas.getMetric() == 1) {
            metric = "Kg";
        }else {
            metric = "Tons";
        }
        final String name = String.valueOf(gas.getSize()).concat(" ").concat(metric);
        holder.name.setText(name);
        holder.price.setText("Kes ".concat(String.valueOf(gas.getPrice())));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView name;
        public final TextView price;
        public final ImageButton add;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.bulk_text);
            price = (TextView) itemView.findViewById(R.id.bulk_price);
            add = (ImageButton) itemView.findViewById(R.id.bulk_gas_more_info);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BulkGas bulkGas = gases.get(getAdapterPosition());
            Cart.getInstance().addBulkGas(bulkGas);
            String metric;
            if(bulkGas.getMetric() == 1) {
                metric = "Kg";
            }else {
                metric = "Tons";
            }
            String name = String.valueOf(bulkGas.getSize()).concat(" ").concat(metric);
            Snackbar snackbar = Snackbar.make(add, name, Snackbar.LENGTH_LONG);
            snackbar.setAction("View Cart", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GECartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            snackbar.show();
        }
    }

}
