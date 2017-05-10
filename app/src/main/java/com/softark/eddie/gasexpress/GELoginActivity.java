package com.softark.eddie.gasexpress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softark.eddie.gasexpress.core.ApplicationConfiguration;
import com.softark.eddie.gasexpress.data.UserData;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.helpers.Internet;

public class GELoginActivity extends AppCompatActivity implements Internet.ConnectivityReceiverListener {

    private EditText phone;
    private UserData userData;
    private GEPreference preference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelogin);

        preference = new GEPreference(this);

        if(preference.isUserLogged()) {
            Intent intent = new Intent(this, GasExpress.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        userData = new UserData(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        phone = (EditText) findViewById(R.id.login_customer_phone);
        FloatingActionButton loginButton = (FloatingActionButton) findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneText = phone.getText().toString().trim();
                if(phoneText.isEmpty()) {
                    final Dialog dialog = new Dialog(GELoginActivity.this);
                    dialog.setContentView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.login_dialog_view, null));
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else {
                    submitDetails(phoneText);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void submitDetails(String phone) {
        if(Internet.isConnected()) {
            progressDialog.setMessage("Validating...");
            progressDialog.show();
            userData.authUser(this.phone, progressDialog, phone);
        }else {
            showSnack();
        }
    }

    @Override
    public void onNetworkConnectionChange(boolean isConnected) {
        checkConnection(isConnected);
    }

    public void checkConnection(boolean isConnected) {
        if(!isConnected) {
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            showSnack();
        }
    }

    public void showSnack() {
        String message = "";
        message = "No internet.Please check your connection";
        Snackbar snackbar = Snackbar.make(phone, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationConfiguration.getInstance().setConnectivityListener(this);
    }
}
