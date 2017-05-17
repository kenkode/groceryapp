package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.models.OrderItem;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final ArrayList<OrderItem> orderItems;
    private final LayoutInflater layoutInflater;

    public ItemAdapter(Context context, ArrayList<OrderItem> orderItems) {
        Context context1 = context;
        this.orderItems = orderItems;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.order_item_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.name.setText(orderItem.getName());
        holder.qty.setText(String.valueOf(orderItem.getQuantity()));
        holder.price.setText(String.valueOf(orderItem.getPrice()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView qty;
        public final TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            name =  (TextView) itemView.findViewById(R.id.order_item_name);
            qty =  (TextView) itemView.findViewById(R.id.order_item_qty);
            price =  (TextView) itemView.findViewById(R.id.order_item_price);
        }
    }

}
