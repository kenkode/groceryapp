package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.softark.eddie.gasexpress.adapters.HistoryAdapter;
import com.softark.eddie.gasexpress.data.History;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class PreviousPurchasesActivity extends AppCompatActivity {

    private RecyclerView historyRecycler;
    private History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_purchases);
        history = new History(this);

        historyRecycler = (RecyclerView) findViewById(R.id.history_list);
        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);
        historyRecycler.addItemDecoration(decorator);

        history.getHistory(historyRecycler);
    }
}
