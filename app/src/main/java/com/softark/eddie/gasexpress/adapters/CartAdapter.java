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
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Gas;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eddie on 5/3/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Gas> items;
    private LayoutInflater inflater;

    public CartAdapter(Context context, ArrayList<Gas> items) {
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
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, int position) {
        final Gas item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                item.decQuantity();
                Cart.removeGas(item);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                if(item.getQuantity() <= 0) {
                    Toast.makeText(context, item.getName().concat(" removed from cart"), Toast.LENGTH_LONG).show();
                    notifyItemRemoved(holder.getAdapterPosition());
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
