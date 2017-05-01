package com.softark.eddie.gasexpress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GELoginActivity extends AppCompatActivity {

    private EditText pin, phone;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelogin);

        pin = (EditText) findViewById(R.id.login_customer_pin);
        phone = (EditText) findViewById(R.id.login_customer_phone);
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinText = pin.getText().toString().trim();
                String phoneText = phone.getText().toString().trim();
                if(pinText.isEmpty() || phoneText.isEmpty()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(GELoginActivity.this);
                    dialog.setView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.login_dialog_view, null));
                    dialog.show();
                }else {

                }
            }
        });




    }
}
