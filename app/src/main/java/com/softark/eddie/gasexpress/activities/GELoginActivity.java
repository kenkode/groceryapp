package com.softark.eddie.gasexpress.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.core.ApplicationConfiguration;
import com.softark.eddie.gasexpress.data.UserData;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.helpers.Internet;
import com.softark.eddie.gasexpress.models.User;
import com.softark.eddie.gasexpress.models.UserAuth;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class GELoginActivity extends AppCompatActivity implements Internet.ConnectivityReceiverListener {

    private IntlPhoneInput phone;
    private GEPreference preference;
    private ProgressDialog progressDialog;
    private FloatingActionButton loginButton;
    private RequestSingleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelogin);

        phone = (IntlPhoneInput) findViewById(R.id.login_customer_phone);
        phone.setEmptyDefault("KE");
        loginButton = (FloatingActionButton) findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        singleton = new RequestSingleton(this);

        preference = new GEPreference(this);

        if (preference.isUserLogged()) {
            startActivity(new Intent(this, GasExpress.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = GELoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
                }
                if (phone.getPhoneNumber() == null) {
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
                } else {
                    String phoneText = String.valueOf(phone.getPhoneNumber().getCountryCode())
                            .concat(String.valueOf(phone.getPhoneNumber().getNationalNumber()));
                    if (phone.isValid()) {
                        submitDetails(phoneText);
                    } else {
                        Toast.makeText(GELoginActivity.this, "Invalid phone number.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void submitDetails(String phone) {
        if (Internet.isConnected()) {
            progressDialog.setMessage("Validating...");
            progressDialog.show();
            authUser(this.phone, progressDialog, phone);
        } else {
            showSnack();
        }
    }

    @Override
    public void onNetworkConnectionChange(boolean isConnected) {
        checkConnection(isConnected);
    }

    private void checkConnection(boolean isConnected) {
        if (!isConnected) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            showSnack();
        }
    }

    private void showSnack() {
        String message = "No internet.Please check your connection";
        Snackbar snackbar = Snackbar.make(phone, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationConfiguration.getInstance().setConnectivityListener(this);
    }

    private void authUser(final IntlPhoneInput phoneTextView, final ProgressDialog dialog, final String phone) {

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);
        Call<UserAuth> userAuthCall = retrofitInterface.authUser(phone);

        userAuthCall.enqueue(new Callback<UserAuth>() {
            @Override
            public void onResponse(Call<UserAuth> call, retrofit2.Response<UserAuth> response) {
                dialog.dismiss();
                UserAuth userAuth = response.body();
                if (userAuth.getStatus().equals("E")) {
                    confirmPin(userAuth, dialog);
                } else {
                    Intent intent = new Intent(GELoginActivity.this, GERegisterActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
                Toast.makeText(GELoginActivity.this, response.body().getStatus(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserAuth> call, Throwable t) {
                Toast.makeText(GELoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void processResults(UserAuth userAuth, String phone) {
        if (userAuth.getStatus().equals("E")) {
            String id = userAuth.getUser().getId();
            String fname = userAuth.getUser().getFname();
            String lname = userAuth.getUser().getLname();
            String phn = userAuth.getUser().getPhone();
            String email = userAuth.getUser().getEmail();
            preference.setUser(id, fname, lname, phn, email);
            Intent intent = new Intent(GELoginActivity.this, GasExpress.class);
            startActivity(intent);
            finish();
        } else if (userAuth.getStatus().equals("DNE")) {
            Intent intent = new Intent(GELoginActivity.this, GERegisterActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
        } else if (userAuth.getStatus().equals("EE")) {
            Toast.makeText(GELoginActivity.this, "Email exists", Toast.LENGTH_LONG).show();
        }
    }

    private void confirmPin(final UserAuth user, final ProgressDialog dialog) {
        final String pin = user.getPin();
        dialog.dismiss();
        final Dialog dialog1 = new Dialog(GELoginActivity.this);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.pin_input_dialog);
        Button cancel = (Button) dialog1.findViewById(R.id.cancel);
        Button submit = (Button) dialog1.findViewById(R.id.ok);
        final EditText pinText = (EditText) dialog1.findViewById(R.id.pin_edit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinText.getText().toString().isEmpty()) {
                    Toast.makeText(GELoginActivity.this, "Insert pin.", Toast.LENGTH_LONG).show();
                } else {
                    if (pinText.getText().toString().equals(pin.trim())) {
                        String recPin = phone.getText().trim();
                        processResults(user, recPin);
                    } else {
                        Toast.makeText(GELoginActivity.this, "Incorrect pin.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        dialog1.show();
    }


}
