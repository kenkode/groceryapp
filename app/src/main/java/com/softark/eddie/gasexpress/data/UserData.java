package com.softark.eddie.gasexpress.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.GERegisterActivity;
import com.softark.eddie.gasexpress.GasExpress;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.helpers.Checkout;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 5/1/2017.
 */

public class UserData {

    private RequestSingleton singleton;
    private Context context;
    private GEPreference preference;

    public UserData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void authUser(final TextView phoneTextView, final ProgressDialog dialog, final String phone) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.AUTH_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("USER", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("E")) {
                                preference.setOrderKey(jsonObject.getString("order_key"));
                                dialog.dismiss();
                            }
                            processResults(jsonObject, phone);
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
                        message = "Something went wrong. Please try again.";
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

    public void addUser(final String name, final String email, final String phone, final String birthday, final String description, final Location location) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            processResults(jsonObject, phone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void processResults(JSONObject jsonObject, String phone) {
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
                context.startActivity(intent);
            }else if(jsonObject.getString("status").equals("DNE")) {
                Intent intent = new Intent(context, GERegisterActivity.class);
                intent.putExtra("phone", phone);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else if(jsonObject.getString("status").equals("EE")) {
                Toast.makeText(context, "Email exists", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void populateDatabase() {

    }


}
