package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.softark.eddie.gasexpress.data.OrderData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class PreviousPurchasesActivity extends AppCompatActivity {

    private RecyclerView historyRecycler;
    private OrderData orderData;
    private LinearLayout historyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_purchases);
        orderData = new OrderData(this);

        historyState = (LinearLayout) findViewById(R.id.empty_history_layout);

        historyRecycler = (RecyclerView) findViewById(R.id.history_list);
        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);
        historyRecycler.addItemDecoration(decorator);

        orderData.getOrders(historyRecycler, historyState);
    }
}
