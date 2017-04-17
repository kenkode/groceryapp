package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GEOrderActivity extends AppCompatActivity {

    private EditText selectLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_georder);

        selectLocation = (EditText) findViewById(R.id.order_location);

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GEOrderActivity.this, GEOrderLocation.class));
            }
        });
    }
}
