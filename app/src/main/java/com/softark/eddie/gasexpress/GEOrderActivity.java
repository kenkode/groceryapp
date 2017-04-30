package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.data.OrderData;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Location;

import java.util.List;

public class GEOrderActivity extends AppCompatActivity {

    private Spinner selectLocation, distributor, size;
    private OrderData orderData;
    private Button placeOrder, locationBtn;
    public static final int LOCATION = 1022;
    private MyLocationData locationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_georder);
        orderData = new OrderData(this);
        locationData = new MyLocationData(this);

        selectLocation = (Spinner) findViewById(R.id.order_location);
        distributor = (Spinner) findViewById(R.id.order_distributor);
        size = (Spinner) findViewById(R.id.order_size);
        placeOrder = (Button) findViewById(R.id.order_button);

        orderData.populateSpinners(distributor, size, selectLocation);

        Intent intent = getIntent();
        Gas gas = intent.getParcelableExtra(Constants.GAS);

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
//                startActivity(new Intent(GEOrderActivity.this, GELocation.class));
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == LOCATION) {
            if(resultCode == RESULT_OK) {
                Location location = data.getParcelableExtra("location");
                if(location != null) {
                    locationData.addLocation(location);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_location_item:
                Intent intent = new Intent(GEOrderActivity.this, GELocation.class);
                startActivityForResult(intent, LOCATION);
                break;
        }
        return true;
    }
}
