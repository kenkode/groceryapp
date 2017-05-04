package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.GEOrderActivity;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.models.Gas;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eddie on 4/16/2017.
 */

public class PriceAdapter extends BaseAdapter {

    private ArrayList<Gas> gases;
    private LayoutInflater inflater;
    private Context context;

    public PriceAdapter(Context context, ArrayList<Gas> gases) {
        this.gases = gases;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gases.size();
    }

    @Override
    public Object getItem(int position) {
        return gases.get(position);
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

        final Gas gas = gases.get(position);

        TextView gasName= (TextView) convertView.findViewById(R.id.gas_name);
        TextView gasPrice = (TextView) convertView.findViewById(R.id.gas_price);
        ImageButton purchase = (ImageButton) convertView.findViewById(R.id.purchase_button);

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, GEOrderActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(Constants.GAS, gas);
//                context.startActivity(intent);
            }
        });

        gasName.setText(String.valueOf(gas.getName()));
        gasPrice.setText(String.valueOf(gas.getPrice()));

        return convertView;

    }

}
