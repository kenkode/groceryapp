package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.data.GasData;

public class PriceActivity extends AppCompatActivity {

    private ListView listView;
    private GasData gas;
    private LinearLayout errorLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout_price);
        errorLayout.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.load_prices);

        Intent intent = getIntent();
        int size = intent.getIntExtra(Constants.SIZE, 0);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(size + " Kg");
        }

        listView = (ListView) findViewById(R.id.price_list);
        gas = new GasData(this);
        gas.getGases(size, listView, errorLayout, progressBar);
    }
}