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
import android.widget.Toast;

import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.GEOrderActivity;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.Gas;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eddie on 4/16/2017.
 */

public class GasAdapter extends BaseAdapter {

    private ArrayList<Gas> gases;
    private LayoutInflater inflater;
    private Context context;

    public GasAdapter(Context context, ArrayList<Gas> gases) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.price_list_layout, null);
        }

        final Gas gas = gases.get(position);

        TextView gasName= (TextView) convertView.findViewById(R.id.gas_name);
        TextView gasPrice = (TextView) convertView.findViewById(R.id.gas_price);
        ImageButton addToCart = (ImageButton) convertView.findViewById(R.id.purchase_button);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, gas.getName().concat(" added to cart"), Toast.LENGTH_LONG).show();
                Cart.getInstance().addGas(gas).addQuantity();
            }
        });

        gasName.setText(String.valueOf(gas.getName()));
        gasPrice.setText(String.valueOf(gas.getPrice()));

        return convertView;

    }

}
