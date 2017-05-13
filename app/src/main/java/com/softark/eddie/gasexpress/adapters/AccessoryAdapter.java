package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.Accessory;

import java.util.ArrayList;

public class AccessoryAdapter extends RecyclerView.Adapter<AccessoryAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Accessory> items;
    private final LayoutInflater inflater;

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
    public AccessoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.accessories_list, null);
        return new AccessoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccessoryAdapter.ViewHolder holder, int position) {
        final Accessory accessory = items.get(position);
        holder.name.setText(accessory.getName());
        holder.price.setText(String.valueOf(accessory.getPrice()));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView name;
        public final TextView price;
        public final ImageButton add;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.accessories_name);
            price = (TextView) itemView.findViewById(R.id.accessories_price);
            add = (ImageButton) itemView.findViewById(R.id.add_to_cart);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Cart.getInstance().addProduct(items.get(getAdapterPosition()));
        }
    }

}
