package com.softark.eddie.gasexpress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.RealmData.AccessoryData;
import com.softark.eddie.gasexpress.RealmData.BulkData;
import com.softark.eddie.gasexpress.RealmData.GasData;
import com.softark.eddie.gasexpress.RealmData.ServiceData;
import com.softark.eddie.gasexpress.adapters.BulkGasAdapter;
import com.softark.eddie.gasexpress.adapters.CartAccessoryAdapter;
import com.softark.eddie.gasexpress.adapters.CartAdapter;
import com.softark.eddie.gasexpress.adapters.CartBulkGasAdapter;
import com.softark.eddie.gasexpress.adapters.CartServiceAdapter;
import com.softark.eddie.gasexpress.data.CartData;
import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.data.OrderData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.Checkout;
import com.softark.eddie.gasexpress.models.CartItem;

import io.realm.Realm;
import io.realm.RealmResults;

public class GECartActivity extends AppCompatActivity {

    private RecyclerView shoppingList, accessoryList, serviceList, bulkGasList;
    private CartData cartData;
    private CartAdapter adapter;
    private CartServiceAdapter serviceAdapter;
    private CartAccessoryAdapter accessoryAdapter;
    private CartBulkGasAdapter bulkGasAdapter;
    private Button clearCart, checkout;
    private GasData gasData;
    private ServiceData serviceData;
    private AccessoryData accessoryData;
    private BulkData bulkData;
    private LinearLayout emptyCartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        gasData = new GasData(this);
        serviceData = new ServiceData();
        accessoryData = new AccessoryData();
        bulkData = new BulkData();

        adapter = new CartAdapter(this, gasData.getGases());
        serviceAdapter = new CartServiceAdapter(this, serviceData.getServices());
        accessoryAdapter = new CartAccessoryAdapter(this, accessoryData.getAccessories());
        bulkGasAdapter = new CartBulkGasAdapter(this, bulkData.getBulkGases());

        emptyCartLayout = (LinearLayout) findViewById(R.id.empty_cart_layout);

        Realm realm = Realm.getDefaultInstance();

        RealmResults<CartItem> cartItems = realm.where(CartItem.class)
                .equalTo("status", 0)
                .findAll();

        if(!cartItems.isEmpty()) {
            emptyCartLayout.setVisibility(View.GONE);
        }else {
            emptyCartLayout.setVisibility(View.VISIBLE);
        }

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

        bulkGasList = (RecyclerView) findViewById(R.id.bulk_gas_list);
        bulkGasList.addItemDecoration(decorator);
        bulkGasList.setAdapter(bulkGasAdapter);

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(GECartActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.cart_clear_cart_dialog);
                Button negative = (Button) dialog.findViewById(R.id.no);
                Button positive = (Button) dialog.findViewById(R.id.yes);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cart.clearCart();
                        clearCart.setEnabled(false);
                        checkout.setEnabled(false);
                        startActivity(new Intent(GECartActivity.this, GasExpress.class));
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
                TextView total = (TextView) dialog.findViewById(R.id.total_text);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                Button checkout = (Button) dialog.findViewById(R.id.check_out);
                final Spinner spinner = (Spinner) dialog.findViewById(R.id.location_spinner);
                MyLocationData myLocationData = new MyLocationData(GECartActivity.this);
                myLocationData.getLocation(null, spinner, null, null);
                total.setText(String.valueOf(Cart.getTotalPrice()));
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Checkout.getLocation() == null) {
                            Toast.makeText(GECartActivity.this, "Please select your location", Toast.LENGTH_LONG).show();
                        }else {
                            Checkout c = new Checkout(GECartActivity.this);
                            c.processOrder();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

    }
}
