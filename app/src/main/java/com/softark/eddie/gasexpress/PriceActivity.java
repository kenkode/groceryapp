package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.softark.eddie.gasexpress.adapters.PriceAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class PriceActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        getSupportActionBar().setTitle("Gas Prices");

        listView = (ListView) findViewById(R.id.price_list);

        ArrayList<HashMap<String, String>> p = new ArrayList<>();

        String[] size = {"6Kg", "8kg", "10Kg", "12Kg", "15Kg"};
        String[] prices = {"1000", "1500", "2000", "2500", "3000"};

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> price = new HashMap<>();
            price.put("size", size[i]);
            price.put("price", prices[i]);
            p.add(price);
        }

        PriceAdapter adapter = new PriceAdapter(this, p);
        listView.setAdapter(adapter);
    }
}
