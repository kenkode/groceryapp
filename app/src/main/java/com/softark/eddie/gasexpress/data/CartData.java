package com.softark.eddie.gasexpress.data;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Eddie on 5/3/2017.
 */

public class CartData {

    private RequestSingleton singleton;
    private Context context;

    public CartData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
    }

    public void checkOutGases(Gas gas) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void checkOutProducts(Accessory accessory) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void checkOutServices(Service service) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

}
