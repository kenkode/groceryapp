package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.softark.eddie.gasexpress.adapters.AccessoryAdapter;
import com.softark.eddie.gasexpress.adapters.ServiceAdapter;
import com.softark.eddie.gasexpress.data.AccessoryServiceData;
import com.softark.eddie.gasexpress.data.ServiceData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class GEAccessoryActivity extends AppCompatActivity {

    private RecyclerView accessoriesList, serviceList;
    private AccessoryServiceData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geaccessory);

        data = new AccessoryServiceData(this);

        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);
        accessoriesList = (RecyclerView) findViewById(R.id.accessories_list);
        accessoriesList.addItemDecoration(decorator);

        RecyclerDecorator serviceDecorator = new RecyclerDecorator(this, 2, 4, true);
        serviceList = (RecyclerView) findViewById(R.id.service_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        serviceList.setLayoutManager(gridLayoutManager);
        serviceList.addItemDecoration(serviceDecorator);

        data.getAccService(accessoriesList, serviceList);

    }
}
