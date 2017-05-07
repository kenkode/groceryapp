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
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;

/**
 * Created by Eddie on 5/7/2017.
 */

public class CartBulkGasAdapter extends RecyclerView.Adapter<CartBulkGasAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BulkGas> items;
    private LayoutInflater inflater;

    public CartBulkGasAdapter(Context context) {
        this.context = context;
        this.items = (ArrayList<BulkGas>) Cart.getInstance().getCart().get(Cart.BULK_GAS);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public CartBulkGasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list, null);
        return new CartBulkGasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartBulkGasAdapter.ViewHolder holder, int position) {
        final BulkGas bulkGas = items.get(position);
        final int refPosition = position;
        final String metric;
        if(bulkGas.getMetric() == 1) {
            metric = "Kg";
        }else {
            metric = "Tons";
        }
        holder.name.setText("Bulk Gas".concat(" ").concat(String.valueOf(bulkGas.getSize())).concat(metric));
        holder.quantity.setText(String.valueOf(bulkGas.getQuantity()));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bulkGas.decQuantity();
                Cart.removeBulkGas(refPosition);
                holder.quantity.setText(String.valueOf(bulkGas.getQuantity()));
                if(bulkGas.getQuantity() <= 0) {
                    Toast.makeText(context, "Bulk Gas"
                            .concat(String.valueOf(bulkGas.getSize()))
                            .concat(metric)
                            .concat(" removed from cart"), Toast.LENGTH_LONG).show();
                    notifyItemRemoved(refPosition);
                    notifyDataSetChanged();
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, quantity;
        public ImageButton remove;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cart_item_name);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            quantity = (TextView) itemView.findViewById(R.id.item_quantity);
            remove = (ImageButton) itemView.findViewById(R.id.remove_from_cart);
        }
    }
}
