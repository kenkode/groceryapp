package com.softark.eddie.gasexpress.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.GasData;

public class PriceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        LinearLayout errorLayout = (LinearLayout) findViewById(R.id.error_layout_price);
        errorLayout.setVisibility(View.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.load_prices);

        Intent intent = getIntent();
        int size = intent.getIntExtra(Constants.SIZE, 0);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(size + " Kg");
        }

        ListView listView = (ListView) findViewById(R.id.price_list);
        GasData gas = new GasData(this);
        gas.getGases(size, listView, errorLayout, progressBar);
    }
}