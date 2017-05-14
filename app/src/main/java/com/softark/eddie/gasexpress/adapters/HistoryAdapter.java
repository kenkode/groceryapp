package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.OrderData;
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

        holder.orderType.setText(h.getOrderType());
        holder.price.setText(String.valueOf(h.getPrice()));
        holder.date.setText(h.getDate());
        OrderHistory history = orderHistory.get(holder.getAdapterPosition());
        OrderData orderData = new OrderData(context);
        orderData.getOrderItems(history.getId(), holder.itemList);
        holder.itemList.setVisibility(View.GONE);
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_list_layout, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView orderType;
        public final TextView price;
        public final TextView date;
        public final RecyclerView itemList;
        public final View view;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            itemList = (RecyclerView) itemView.findViewById(R.id.item_list);
            orderType = (TextView) itemView.findViewById(R.id.order_type);
            price = (TextView) itemView.findViewById(R.id.order_price);
            date = (TextView) itemView.findViewById(R.id.purchase_date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemList.getVisibility() == View.VISIBLE) {
                itemList.setVisibility(View.GONE);
            }else {
                itemList.setVisibility(View.VISIBLE);
            }
        }
    }

}
