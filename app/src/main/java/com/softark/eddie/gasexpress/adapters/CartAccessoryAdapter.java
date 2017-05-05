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

/**
 * Created by Eddie on 5/5/2017.
 */

public class CartAccessoryAdapter extends RecyclerView.Adapter<CartAccessoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Accessory> items;
    private LayoutInflater inflater;

    public CartAccessoryAdapter(Context context) {
        this.context = context;
        this.items = (ArrayList<Accessory>) Cart.getInstance().getCart().get(Cart.ACCESSORIES);
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
    public CartAccessoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list, null);
        return new CartAccessoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartAccessoryAdapter.ViewHolder holder, final int position) {
        final Accessory item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageButton remove;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cart_item_name);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            remove = (ImageButton) itemView.findViewById(R.id.remove_from_cart);
        }
    }
}
