package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eddie on 4/17/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> history;
    private LayoutInflater inflater;

    public HistoryAdapter(Context context, ArrayList<HashMap<String, String>> history) {
        this.context = context;
        this.history = history;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        HashMap<String, String> hist = history.get(position);

        holder.gasType.setText(hist.get("type"));
        holder.gasCost.setText(hist.get("price"));
        holder.arrTime.setText(hist.get("time"));
        holder.purDate.setText(hist.get("date"));

    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_list_layout, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gasType, arrTime, gasCost, purDate;

        public ViewHolder(View itemView) {
            super(itemView);

            gasCost = (TextView) itemView.findViewById(R.id.hist_gas_price);
            gasType = (TextView) itemView.findViewById(R.id.hist_gas_type);
            arrTime = (TextView) itemView.findViewById(R.id.hist_gas_arrival_time);
            purDate = (TextView) itemView.findViewById(R.id.hist_gas_purchase_date);

        }
    }

}
