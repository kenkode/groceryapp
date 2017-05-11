package com.softark.eddie.gasexpress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softark.eddie.gasexpress.core.ApplicationConfiguration;
import com.softark.eddie.gasexpress.data.UserData;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.helpers.Internet;

public class GELoginActivity extends AppCompatActivity implements Internet.ConnectivityReceiverListener {

    private EditText phone;
    private UserData userData;
    private GEPreference preference;
    private ProgressDialog progressDialog;
    private FloatingActionButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelogin);

        phone = (EditText) findViewById(R.id.login_customer_phone);
        loginButton = (FloatingActionButton) findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        userData = new UserData(this);

        preference = new GEPreference(this);

        if(preference.isUserLogged()) {
            if(Internet.isConnected()) {
                userData.validateUser(progressDialog, phone);
            }else {
                showSnack("No internet connection", Snackbar.LENGTH_INDEFINITE);
            }
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = GELoginActivity.this.getCurrentFocus();
                if(view != null) {
                    InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
                }
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
                    if(phoneText.matches("\\d{10}")){
                        submitDetails(phoneText);
                    }else {
                        Toast.makeText(GELoginActivity.this, "Invalid phone number.", Toast.LENGTH_LONG).show();
                    }
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
            showSnack("No internet.Please check your connection", Snackbar.LENGTH_LONG);
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
            showSnack("No internet.Please check your connection", Snackbar.LENGTH_LONG);
        }else {

        }
    }

    public void showSnack(String message, int length) {
        Snackbar snackbar = Snackbar.make(phone, message, length);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationConfiguration.getInstance().setConnectivityListener(this);
    }
}
