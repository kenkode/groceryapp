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
import com.softark.eddie.gasexpress.adapters.BulkGasAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.BulkGas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BulkGasData {

    private final Context context;
    private final GEPreference preference;
    private final RequestSingleton singleton;

    public BulkGasData(Context context) {
        this.context = context;
        preference = new GEPreference(context);
        singleton = new RequestSingleton(context);
    }

    public void getBulkGases(final RecyclerView recyclerView, final LinearLayout errorLayout, final ProgressBar loader) {

        final ArrayList<BulkGas> gases = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_BULK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray gasArray = new JSONArray(response);
                            loader.setVisibility(View.GONE);
                            if(gasArray.length() <= 0) {
                                errorLayout.setVisibility(View.VISIBLE);
                            }else {
                                errorLayout.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < gasArray.length(); i++) {
                                JSONObject gasObject = gasArray.getJSONObject(i);
                                BulkGas gas = new BulkGas();
                                gas.setId(gasObject.getString("id"));
                                gas.setMetric(gasObject.getInt("metric"));
                                gas.setSize(gasObject.getInt("size"));
                                gas.setPrice(gasObject.getDouble("price"));
                                gases.add(gas);
                            }

                            BulkGasAdapter adapter = new BulkGasAdapter(context, gases);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "Server took long to respond. Please try again later.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        loader.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                        final Snackbar snackbar = Snackbar.make(errorLayout, message, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                                errorLayout.setVisibility(View.GONE);
                                loader.setVisibility(View.VISIBLE);
                                getBulkGases(recyclerView, errorLayout, loader);
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
