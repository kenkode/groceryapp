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
import com.softark.eddie.gasexpress.models.Accessory;

import java.util.ArrayList;

/**
 * Created by Eddie on 5/3/2017.
 */

public class AccessoryAdapter extends RecyclerView.Adapter<AccessoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Accessory> items;
    private LayoutInflater inflater;

    public AccessoryAdapter(Context context, ArrayList<Accessory> items) {
        this.context = context;
        this.items = items;
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
    public AccessoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.accessories_list, null);
        return new AccessoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccessoryAdapter.ViewHolder holder, final int position) {
        final Accessory accessory = items.get(position);
        holder.name.setText(accessory.getName());
        holder.price.setText(String.valueOf(accessory.getPrice()));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, accessory.getName().concat(" added to cart"), Toast.LENGTH_LONG).show();
                Cart.getInstance().addProduct(accessory).addQuantity();

            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageButton add;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.accessories_name);
            price = (TextView) itemView.findViewById(R.id.accessories_price);
            add = (ImageButton) itemView.findViewById(R.id.add_to_cart);
        }
    }

}
