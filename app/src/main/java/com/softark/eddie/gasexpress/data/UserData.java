package com.softark.eddie.gasexpress.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
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
import com.softark.eddie.gasexpress.activities.GERegisterActivity;
import com.softark.eddie.gasexpress.activities.GasExpress;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    private final RequestSingleton singleton;
    private final Context context;
    private final GEPreference preference;

    public UserData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void validateUser(final ProgressDialog dialog, final View button) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.VALIDATE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        button.setVisibility(View.VISIBLE);
                        if(response.equals("E")) {
                            dialog.dismiss();
                            Intent intent = new Intent(context, GasExpress.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
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
                            snackbar.setActionTextColor(context.getResources().getColor(R.color.colorRedAccent));
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
    }

    public void authUser(final TextView phoneTextView, final ProgressDialog dialog, final String phone) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.AUTH_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("USER", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            dialog.dismiss();
                            processResults(jsonObject, phone);
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

    public void addUser(final String name, final String email, final String phone, final String birthday,final View view, final String description, final Location location, final ProgressDialog progressDialog) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ADD_USER", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            processResults(jsonObject, phone);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        String message = "";
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "Server took long to respond. Please try again.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("birthday", birthday);
                params.put("description", description);
                params.put("phone", phone);
                params.put("address", location.getAddress());
                params.put("lat", String.valueOf(location.getLat()));
                params.put("lng", String.valueOf(location.getLng()));
                Log.i("PARAMS", params.toString());
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
                Intent intent = new Intent(context, GasExpress.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }else if(jsonObject.getString("status").equals("DNE")) {
                Intent intent = new Intent(context, GERegisterActivity.class);
                intent.putExtra("phone", phone);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }else if(jsonObject.getString("status").equals("EE")) {
                Toast.makeText(context, "Email exists", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
