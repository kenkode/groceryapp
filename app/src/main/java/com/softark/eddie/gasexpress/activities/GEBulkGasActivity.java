package com.softark.eddie.gasexpress.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.BulkGasData;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.OrderKey;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;

import io.realm.Realm;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static com.softark.eddie.gasexpress.helpers.Cart.BULK_GAS;

public class GEBulkGasActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener  {

    private TextView price, pricePer, totalSize;
    private NumberPicker numberPicker;
    private static BulkGas bulkGas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gebulk_gas);
        Realm realm = Realm.getDefaultInstance();

        price = (TextView) findViewById(R.id.bulk_price_text);
        pricePer = (TextView) findViewById(R.id.price_per);
        totalSize = (TextView) findViewById(R.id.total_size);
        numberPicker = (NumberPicker) findViewById(R.id.bulk_quantity_select);
        Button buy = (Button) findViewById(R.id.bulk_buy);
        numberPicker.setEnabled(false);
        buy.setEnabled(false);
        buy.setOnClickListener(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100000);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);

        CartItem cartItem = realm.where(CartItem.class)
                .equalTo("id", OrderKey.orderKey)
                .equalTo("type", Cart.BULK_GAS)
                .findFirst();

        if(cartItem != null) {
            numberPicker.setValue(cartItem.getQuantity());
            pricePer.setText(String.valueOf(cartItem.getPrice()));
            int ttlPrice = (int) cartItem.getPrice() * cartItem.getQuantity();
            price.setText(String.valueOf(ttlPrice));
            buy.setText(R.string.update_text);
            bulkGas = new BulkGas();
            bulkGas.setId(cartItem.getId());
            bulkGas.setQuantity(cartItem.getQuantity());
            bulkGas.setName(cartItem.getName());
            bulkGas.setPrice(cartItem.getPrice());
        }else {
            bulkGas = null;
        }

        totalSize.setText(String.valueOf(numberPicker.getValue()));

        BulkGasData data = new BulkGasData();
        data.getBulkPrice(price, pricePer, buy, numberPicker);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        int scrollState = 0;
        if(scrollState == SCROLL_STATE_IDLE) {
            double newTotal = 0;
            totalSize.setText(String.valueOf(newVal));
            try {
                int price = (int) Double.parseDouble(pricePer.getText().toString());
                if(bulkGas != null) {
                    bulkGas.setPrice(price);
                    Cart.updateCartItem(bulkGas, BULK_GAS, picker.getValue());
                    bulkGas.setQuantity(picker.getValue());
                }
                newTotal = price * picker.getValue();
            }catch (NumberFormatException e) {
                e.printStackTrace();
            }
            price.setText(String.valueOf(newTotal));
        }
    }

    @Override
    public void onClick(View v) {
        if(bulkGas != null) {
            Cart.updateCartItem(bulkGas, BULK_GAS, numberPicker.getValue());
        }else {
            bulkGas = new BulkGas();
            bulkGas.setName("Bulk Gas");
            bulkGas.setQuantity(numberPicker.getValue());
            bulkGas.setId(OrderKey.orderKey);
            int price = (int) Double.parseDouble(pricePer.getText().toString());
            bulkGas.setPrice(price);
            Cart.addBulkGas(bulkGas);
        }
        Snackbar snackbar = Snackbar.make(pricePer, "Bulk Gas added to orders", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("View Orders", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GEBulkGasActivity.this, GECartActivity.class));
            }
        });
        snackbar.show();
    }
}
