package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eddie on 4/16/2017.
 */

public class PriceAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, String>> prices;
    private LayoutInflater inflater;
    private Context context;

    public PriceAdapter(Context context, ArrayList<HashMap<String, String>> prices) {
        this.prices = prices;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return prices.size();
    }

    @Override
    public Object getItem(int position) {
        return prices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.price_list_layout, null);
        }

        TextView gasSize = (TextView) convertView.findViewById(R.id.gas_size);
        TextView gasPrice = (TextView) convertView.findViewById(R.id.gas_price);

        HashMap<String, String> price = prices.get(position);

        gasSize.setText(price.get("size"));
        gasPrice.setText(price.get("price"));

        return convertView;

    }

}
