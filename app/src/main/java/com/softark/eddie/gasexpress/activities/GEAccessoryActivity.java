package com.softark.eddie.gasexpress.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.AccessoryServiceData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class GEAccessoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geaccessory);

        AccessoryServiceData data = new AccessoryServiceData(this);

        LinearLayout errorLayout = (LinearLayout) findViewById(R.id.error_layout_accessories);
        errorLayout.setVisibility(View.GONE);
        ProgressBar loader = (ProgressBar) findViewById(R.id.load_accessories);

        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);
        RecyclerView accessoriesList = (RecyclerView) findViewById(R.id.accessories_list);
        accessoriesList.addItemDecoration(decorator);

        RecyclerDecorator serviceDecorator = new RecyclerDecorator(this, 2, 4, true);
        RecyclerView serviceList = (RecyclerView) findViewById(R.id.service_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        serviceList.setLayoutManager(gridLayoutManager);
        serviceList.addItemDecoration(serviceDecorator);

        data.getServices(serviceList, errorLayout, loader);
        data.getAccessories(accessoriesList, errorLayout, loader);

    }
}
