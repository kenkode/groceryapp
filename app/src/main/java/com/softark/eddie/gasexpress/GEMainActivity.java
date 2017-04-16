package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.softark.eddie.gasexpress.adapters.DistributorAdapter;
import com.softark.eddie.gasexpress.data.Distributors;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class GEMainActivity extends AppCompatActivity {

    private RecyclerView distributorRecyclerView;
    private DistributorAdapter adapter;
    private Distributors distributors;
    private RecyclerDecorator recyclerDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gemain);

        recyclerDecorator = new RecyclerDecorator(this, 2, 10, true);

        distributorRecyclerView = (RecyclerView) findViewById(R.id.distr_list);
        distributorRecyclerView.addItemDecoration(recyclerDecorator);
        distributorRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        distributors = new Distributors();

        adapter = new DistributorAdapter(distributors.getDistributors(), this);

        distributorRecyclerView.setAdapter(adapter);

    }
}
