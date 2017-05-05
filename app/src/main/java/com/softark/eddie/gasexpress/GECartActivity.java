package com.softark.eddie.gasexpress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.softark.eddie.gasexpress.adapters.CartAccessoryAdapter;
import com.softark.eddie.gasexpress.adapters.CartAdapter;
import com.softark.eddie.gasexpress.adapters.CartServiceAdapter;
import com.softark.eddie.gasexpress.data.CartData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.helpers.Cart;

public class GECartActivity extends AppCompatActivity {

    private RecyclerView shoppingList, accessoryList, serviceList;
    private CartData cartData;
    private CartAdapter adapter;
    private CartServiceAdapter serviceAdapter;
    private CartAccessoryAdapter accessoryAdapter;
    private Button clearCart, checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        adapter = new CartAdapter(this);
        serviceAdapter = new CartServiceAdapter(this);
        accessoryAdapter = new CartAccessoryAdapter(this);

        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);

        clearCart = (Button) findViewById(R.id.clear_cart);
        checkout = (Button) findViewById(R.id.check_out);
        shoppingList = (RecyclerView) findViewById(R.id.shopping_list);
        shoppingList.addItemDecoration(decorator);
        shoppingList.setAdapter(adapter);

        serviceList = (RecyclerView) findViewById(R.id.service_list);
        serviceList.addItemDecoration(decorator);
        serviceList.setAdapter(serviceAdapter);

        accessoryList = (RecyclerView) findViewById(R.id.accessories_list);
        accessoryList.addItemDecoration(decorator);
        accessoryList.setAdapter(accessoryAdapter);

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(GECartActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Confirm");
                dialog.setMessage("Are you sure you want to clear the cart");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cart.getInstance().clearCart();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(GECartActivity.this);
                dialog.setContentView(R.layout.checkout_dialog);
                dialog.setCancelable(false);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                Button checkout = (Button) dialog.findViewById(R.id.check_out);
                Spinner spinner = (Spinner) dialog.findViewById(R.id.location_spinner);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                dialog.show();
            }
        });

    }
}
