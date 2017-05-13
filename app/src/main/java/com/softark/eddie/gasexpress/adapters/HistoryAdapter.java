package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.models.OrderHistory;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<OrderHistory> orderHistory;
    private final LayoutInflater inflater;

    public HistoryAdapter(Context context, ArrayList<OrderHistory> orderHistory) {
        this.context = context;
        this.orderHistory = orderHistory;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return orderHistory.size();
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        OrderHistory h = orderHistory.get(position);

        holder.orderId.setText(h.getOrderType());
        holder.price.setText(String.valueOf(h.getPrice()));
        holder.date.setText(h.getDate());
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_list_layout, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView orderId;
        public final TextView price;
        public final TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.order_id);
            price = (TextView) itemView.findViewById(R.id.order_price);
            date = (TextView) itemView.findViewById(R.id.purchase_date);

        }
    }

}
