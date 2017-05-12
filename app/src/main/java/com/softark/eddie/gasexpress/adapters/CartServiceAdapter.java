package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;

/**
 * Created by Eddie on 5/5/2017.
 */

public class CartServiceAdapter extends RecyclerView.Adapter<CartServiceAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Service> items;
    private LayoutInflater inflater;

    public CartServiceAdapter(Context context, ArrayList<Service> services) {
        this.context = context;
        this.items = services;
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
    public CartServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list, null);
        return new CartServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartServiceAdapter.ViewHolder holder, int position) {
        final Service service = items.get(position);
        final int refPosition = position;
        holder.name.setText(service.getName());
        holder.price.setText(service.getName());

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price;
        public ImageButton remove;
        public NumberPicker numberPicker;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cart_item_name);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            remove = (ImageButton) itemView.findViewById(R.id.remove_from_cart);
            remove.setOnClickListener(this);
            numberPicker = (NumberPicker) itemView.findViewById(R.id.quantity_select);
            numberPicker.setEnabled(false);
        }

        @Override
        public void onClick(View v) {
            Cart.removeService(items.get(getAdapterPosition()));
            items.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyDataSetChanged();
        }
    }

}
