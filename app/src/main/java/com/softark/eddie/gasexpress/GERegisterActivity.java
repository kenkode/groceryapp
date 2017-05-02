package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class GERegisterActivity extends AppCompatActivity {

    private EditText name, email, phone, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geregister);

        phone = (EditText) findViewById(R.id.register_customer_phone);

        Intent intent = getIntent();
        if(intent != null) {
            phone.setText(intent.getStringExtra("phone"));
        }

    }
}
