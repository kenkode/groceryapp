package com.softark.eddie.gasexpress.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.RealmData.AccessoryData;
import com.softark.eddie.gasexpress.RealmData.BulkData;
import com.softark.eddie.gasexpress.RealmData.GasData;
import com.softark.eddie.gasexpress.RealmData.ServiceData;
import com.softark.eddie.gasexpress.adapters.CartAccessoryAdapter;
import com.softark.eddie.gasexpress.adapters.CartAdapter;
import com.softark.eddie.gasexpress.adapters.CartBulkGasAdapter;
import com.softark.eddie.gasexpress.adapters.CartServiceAdapter;
import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.Checkout;
import com.softark.eddie.gasexpress.helpers.OrderKey;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.OrderPrice;

import io.realm.Realm;
import io.realm.RealmResults;

public class GECartActivity extends AppCompatActivity {

    private RecyclerView shoppingList, accessoryList, serviceList, bulkGasList;
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
    private TextView totalPrice;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        realm = Realm.getDefaultInstance();

        totalPrice = (TextView) findViewById(R.id.total_price);
        OrderPrice price = realm.where(OrderPrice.class)
                .equalTo("id", OrderKey.orderKey).findFirst();
        if(price != null) {
            totalPrice.setText(String.valueOf(price.getPrice()));
            Cart.totalPrice = price.getPrice();
        }else {
            totalPrice.setText(String.valueOf(Cart.totalPrice));
        }

        gasData = new GasData(this);
        serviceData = new ServiceData();
        accessoryData = new AccessoryData();
        bulkData = new BulkData();

        adapter = new CartAdapter(this, gasData.getGases(), totalPrice);
        serviceAdapter = new CartServiceAdapter(this, serviceData.getServices());
        accessoryAdapter = new CartAccessoryAdapter(this, accessoryData.getAccessories(), totalPrice);
        bulkGasAdapter = new CartBulkGasAdapter(this, bulkData.getBulkGases(), totalPrice);

        emptyCartLayout = (LinearLayout) findViewById(R.id.empty_cart_layout);
        clearCart = (Button) findViewById(R.id.clear_cart);
        checkout = (Button) findViewById(R.id.check_out);

        RealmResults<CartItem> cartItems = realm.where(CartItem.class)
                .equalTo("status", 0)
                .findAll();

        if(!cartItems.isEmpty()) {
            checkout.setEnabled(true);
            clearCart.setEnabled(true);
            emptyCartLayout.setVisibility(View.GONE);
        }else {
            emptyCartLayout.setVisibility(View.VISIBLE);
            checkout.setEnabled(false);
            clearCart.setEnabled(false);
        }

        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);

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

        final RealmResults<CartItem> items = realm.where(CartItem.class)
                .findAll();

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.isEmpty()) {
                    Toast.makeText(GECartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }else {
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
                total.setText(String.valueOf(realm.where(OrderPrice.class).equalTo("id", OrderKey.orderKey).findFirst().getPrice()));
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
                            if(items.isEmpty()) {
                                Toast.makeText(GECartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                            }else {
                                Checkout c = new Checkout(GECartActivity.this);
                                c.processOrder();
                                dialog.dismiss();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });

    }
}
