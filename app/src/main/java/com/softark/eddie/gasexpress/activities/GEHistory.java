package com.softark.eddie.gasexpress.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.OrderData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class GEHistory extends AppCompatActivity {

    private RecyclerView historyRecycler;
    private OrderData orderData;
    private LinearLayout historyState;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_purchases);
        orderData = new OrderData(this);

        loader = (ProgressBar) findViewById(R.id.history_loader);
        historyState = (LinearLayout) findViewById(R.id.empty_history_layout);
        historyState.setVisibility(View.GONE);

        historyRecycler = (RecyclerView) findViewById(R.id.history_list);
        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);
        historyRecycler.addItemDecoration(decorator);

        orderData.getOrders(historyRecycler, historyState, loader);
    }
}
