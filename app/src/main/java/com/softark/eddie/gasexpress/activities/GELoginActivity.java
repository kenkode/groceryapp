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
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.core.ApplicationConfiguration;
import com.softark.eddie.gasexpress.data.UserData;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.helpers.Internet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GELoginActivity extends AppCompatActivity implements Internet.ConnectivityReceiverListener {

    private EditText phone;
    private GEPreference preference;
    private ProgressDialog progressDialog;
    private FloatingActionButton loginButton;
    private RequestSingleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelogin);

        phone = (EditText) findViewById(R.id.login_customer_phone);
        loginButton = (FloatingActionButton) findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        singleton = new RequestSingleton(this);

        preference = new GEPreference(this);

        if(preference.isUserLogged()) {
            startActivity(new Intent(this, GasExpress.class));
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

    private void submitDetails(String phone) {
        if(Internet.isConnected()) {
            progressDialog.setMessage("Validating...");
            progressDialog.show();
            authUser(this.phone, progressDialog, phone);
        }else {
            showSnack();
        }
    }

    @Override
    public void onNetworkConnectionChange(boolean isConnected) {
        checkConnection(isConnected);
    }

    private void checkConnection(boolean isConnected) {
        if(!isConnected) {
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

    private void validateUser(final ProgressDialog dialog, final View button) {
        if(Internet.isConnected()) {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.VALIDATE_USER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            button.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(response.equals("E")) {
                                    confirmPin(jsonObject, dialog);
                                }else {
                                    dialog.dismiss();
                                    preference.unsetUser();
                                    final Snackbar snackbar = Snackbar.make(button, "Account not found in our servers.", Snackbar.LENGTH_INDEFINITE);
                                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                    //noinspection deprecation
                                    snackbar.setActionTextColor(getResources().getColor(R.color.colorRedAccent));
                                    snackbar.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            dialog.dismiss();
                            String message = "";
                            if(error instanceof TimeoutError || error instanceof NetworkError) {
                                message = "Server took long to respond. Please try again later.";
                            }else if(error instanceof ServerError) {
                                message = "Server experienced internal error. Please try again later.";
                            }
                            button.setVisibility(View.GONE);
                            final Snackbar snackbar = Snackbar.make(button, message, Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                    dialog.show();
                                    validateUser(dialog, button);
                                }
                            });
                            snackbar.show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user", preference.getUser().get(GEPreference.USER_ID));
                    return params;
                }
            };
            singleton.addToRequestQueue(stringRequest);
            loginButton.setVisibility(View.VISIBLE);
        }else {
            Snackbar snackbar = Snackbar.make(phone, "No internet connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateUser(dialog, button);
                }
            });
            snackbar.show();
            loginButton.setVisibility(View.INVISIBLE);
        }
    }

    private void authUser(final TextView phoneTextView, final ProgressDialog dialog, final String phone) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.AUTH_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            dialog.dismiss();
                            if(jsonObject.getString("status").equals("E")) {
                                confirmPin(jsonObject, dialog);
                            }else {
                                Intent intent = new Intent(GELoginActivity.this, GERegisterActivity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar.make(phoneTextView, "Error occurred. Try again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        dialog.dismiss();
                        String message = "";
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "Server took long to respond. Please try again.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        Snackbar snackbar = Snackbar.make(phoneTextView, message, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    private void processResults(JSONObject jsonObject, String phone) {
        try {
            if(jsonObject.getString("status").equals("E")) {
                JSONObject user = jsonObject.getJSONObject("user");
                String id = user.getString("id");
                String name = user.getString("name");
                String phn = user.getString("phone");
                String email = user.getString("email");
                preference.setUser(id, name, phn, email);
                Intent intent = new Intent(GELoginActivity.this, GasExpress.class);
                startActivity(intent);
            }else if(jsonObject.getString("status").equals("DNE")) {
                Intent intent = new Intent(GELoginActivity.this, GERegisterActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }else if(jsonObject.getString("status").equals("EE")) {
                Toast.makeText(GELoginActivity.this, "Email exists", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void confirmPin(final JSONObject jsonObject, final ProgressDialog dialog) {
        try {
            final String pin = jsonObject.getString("pin");
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
                    if(pinText.getText().toString().isEmpty()) {
                        Toast.makeText(GELoginActivity.this, "Insert pin.", Toast.LENGTH_LONG).show();
                    }else {
                        if(pinText.getText().toString().equals(pin.trim())) {
                            processResults(jsonObject, phone.getText().toString().trim());
                        }else {
                            Toast.makeText(GELoginActivity.this, "Incorrect pin.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            dialog1.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
