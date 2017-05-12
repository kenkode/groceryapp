package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.AccessoryAdapter;
import com.softark.eddie.gasexpress.adapters.ServiceAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 5/3/2017.
 */

public class AccessoryServiceData {

    private Context context;
    private RequestSingleton singleton;
    private GEPreference preference;

    public AccessoryServiceData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void getAccService(final RecyclerView accessoriesRv, final RecyclerView servicesRv, final LinearLayout errorLayout, final ProgressBar progressBar) {

        final ArrayList<Accessory> accessories = new ArrayList<>();
        final ArrayList<Service> services = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_ACC_SERVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject products = new JSONObject(response);
                            JSONArray accessoriesArray = products.getJSONArray("accessories");
                            JSONArray servicesArray = products.getJSONArray("services");

                            if(accessoriesArray.length() > 0 || servicesArray.length() > 0) {
                                errorLayout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            }else {
                                errorLayout.setVisibility(View.VISIBLE);
                            }

                            for (int i = 0; i < accessoriesArray.length(); i++) {
                                JSONObject accessoryObject = accessoriesArray.getJSONObject(i);
                                Accessory accessory = new Accessory();
                                accessory.setId(accessoryObject.getString("ID"));
                                accessory.setName(accessoryObject.getString("NAME"));
                                accessory.setPrice(accessoryObject.getDouble("PRICE"));
                                accessories.add(accessory);
                            }

                            for (int i = 0; i < servicesArray.length(); i++) {
                                JSONObject accessoryObject = servicesArray.getJSONObject(i);
                                Service service = new Service();
                                service.setId(accessoryObject.getString("id"));
                                service.setName(accessoryObject.getString("NAME"));
                                services.add(service);
                            }

                            AccessoryAdapter accessoryAdapter = new AccessoryAdapter(context, accessories);
                            ServiceAdapter serviceAdapter = new ServiceAdapter(context, services);
                            accessoriesRv.setAdapter(accessoryAdapter);
                            servicesRv.setAdapter(serviceAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        String message = "";
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "Server took long to respond. Please try again later.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }else if (error instanceof NetworkError) {
                            message = "Network error. Please try again later.";
                        }
                        progressBar.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        final Snackbar snackbar = Snackbar.make(errorLayout, message, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                                errorLayout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                getAccService(accessoriesRv, servicesRv, errorLayout, progressBar);
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

}
