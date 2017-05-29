package com.softark.eddie.gasexpress.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.BulkGasData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.OrderKey;
import com.softark.eddie.gasexpress.models.BulkCart;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;

import io.realm.Realm;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static com.softark.eddie.gasexpress.helpers.Cart.totalPrice;

public class GEBulkGasActivity extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener  {

    private TextView price, pricePer, totalSize;
    private NumberPicker numberPicker;
    private Button buy;
    private int scrollState = 0;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gebulk_gas);
        realm = Realm.getDefaultInstance();

        price = (TextView) findViewById(R.id.bulk_price_text);
        pricePer = (TextView) findViewById(R.id.price_per);
        totalSize = (TextView) findViewById(R.id.total_size);
        pricePer.setText("120");
        numberPicker = (NumberPicker) findViewById(R.id.bulk_quantity_select);
        buy = (Button) findViewById(R.id.bulk_buy);
//        numberPicker.setEnabled(false);
//        buy.setEnabled(false);
        buy.setOnClickListener(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100000);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);

        CartItem bulkGas = realm.where(CartItem.class)
                .equalTo("id", OrderKey.orderKey)
                .equalTo("type", Cart.BULK_GAS)
                .findFirst();

        if(bulkGas != null) {
            numberPicker.setValue(bulkGas.getQuantity());
            pricePer.setText(String.valueOf(bulkGas.getPrice()));
            int ttlPrice = (int) bulkGas.getPrice() * bulkGas.getQuantity();
            price.setText(String.valueOf(ttlPrice));
        }

        totalSize.setText(String.valueOf(numberPicker.getValue()));

        BulkGasData data = new BulkGasData(this);
        data.getBulkPrice(pricePer, buy, numberPicker);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if(scrollState == SCROLL_STATE_IDLE) {
//            BulkGas bulkGas = new BulkGas();
            double oldTotal = 0;
            double newTotal = 0;
            totalSize.setText(String.valueOf(newVal));
            try {
                double price = Double.parseDouble(pricePer.getText().toString());
                oldTotal = price * oldVal;
                newTotal = price * newVal;
                Cart.updateBulkPrice(newVal, price, oldTotal, newTotal);
            }catch (NumberFormatException e) {
                e.printStackTrace();
            }
            price.setText(String.valueOf(newTotal));
        }
    }

    @Override
    public void onClick(View v) {
        Snackbar snackbar = Snackbar.make(price, "Bulk Gas added to orders", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("View Orders", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GEBulkGasActivity.this, GECartActivity.class));
            }
        });
    }
}
