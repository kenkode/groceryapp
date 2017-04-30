package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.DistributorAdapter;
import com.softark.eddie.gasexpress.adapters.PriceAdapter;
import com.softark.eddie.gasexpress.models.Gas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 4/30/2017.
 */

public class SizeData {

    private RequestSingleton requestSingleton;
    private Context context;
    private DistributorAdapter adapter;

    public SizeData(Context context) {
        this.context = context;
        requestSingleton = new RequestSingleton(context);
    }

    public void getSizes(final RecyclerView recyclerView) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_SIZES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonObject = new JSONArray(response);
                            String[] sizes = new String[jsonObject.length()];
                            for (int i = 0; i < jsonObject.length(); i++) {
                                sizes[i] = jsonObject.getString(i);
                            }
                            adapter = new DistributorAdapter(sizes, context);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestSingleton.addToRequestQueue(stringRequest);
    }

}
