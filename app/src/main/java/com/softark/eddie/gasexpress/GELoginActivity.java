package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class GELoginActivity extends AppCompatActivity {

    private EditText pin;
    private static int keyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelogin);

        pin = (EditText) findViewById(R.id.login_customer_pin);
        keyCount = 0;

        pin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    keyCount++;
                }
                if(keyCount >= 4) {
                    startActivity(new Intent(GELoginActivity.this, GasExpress.class));
                    keyCount = 0;
                }
                return false;
            }
        });

    }
}
