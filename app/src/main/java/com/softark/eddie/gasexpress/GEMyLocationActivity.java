package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class GEMyLocationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyLocationData locationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gemy_location);

        recyclerView = (RecyclerView) findViewById(R.id.my_location_recy);
        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 8, true);
        recyclerView.addItemDecoration(decorator);

        locationData = new MyLocationData(this);
        locationData.getLocation(recyclerView);

    }
}
