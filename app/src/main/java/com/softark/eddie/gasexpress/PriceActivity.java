package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.softark.eddie.gasexpress.data.GasData;

public class PriceActivity extends AppCompatActivity {

    private ListView listView;
    private GasData gas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        getSupportActionBar().setTitle("GasData Prices");

        Intent intent = getIntent();
        int size = intent.getIntExtra(Constants.SIZE, 0);

        listView = (ListView) findViewById(R.id.price_list);
        gas = new GasData(this);
        gas.getGases(size, listView);
    }
}