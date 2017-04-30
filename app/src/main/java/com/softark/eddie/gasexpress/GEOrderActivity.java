package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.softark.eddie.gasexpress.data.OrderData;

public class GEOrderActivity extends AppCompatActivity {

    private Spinner selectLocation, distributor, size;
    private OrderData orderData;
    private Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_georder);
        orderData = new OrderData(this);

        selectLocation = (Spinner) findViewById(R.id.order_location);
        distributor = (Spinner) findViewById(R.id.order_distributor);
        size = (Spinner) findViewById(R.id.order_size);
        placeOrder = (Button) findViewById(R.id.order_button);

        orderData.populateSpinners(distributor, size, selectLocation);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderData.placeOrder();
                startActivity(new Intent(GEOrderActivity.this, PreviousPurchasesActivity.class));
            }
        });

//        selectLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(GEOrderActivity.this, GEOrderLocation.class));
//            }
//        });
    }
}
