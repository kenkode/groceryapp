package com.softark.eddie.gasexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.activities.GECartActivity;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.Gas;

import java.util.ArrayList;

public class GasAdapter extends BaseAdapter {

    private final ArrayList<Gas> gases;
    private final LayoutInflater inflater;
    private final Context context;

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

        final TextView gasName= (TextView) convertView.findViewById(R.id.gas_name);
        TextView gasPrice = (TextView) convertView.findViewById(R.id.gas_price);
        ImageButton addToCart = (ImageButton) convertView.findViewById(R.id.purchase_button);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(gasName, gas.getName().concat(" added to cart"), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("View Cart", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GECartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                Cart.getInstance().addGas(gas);
                snackbar.show();
            }
        });

        gasName.setText(String.valueOf(gas.getName()));
        gasPrice.setText(String.valueOf(gas.getPrice()));

        return convertView;

    }

}
