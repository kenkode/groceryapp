package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.AccessoryAdapter;
import com.softark.eddie.gasexpress.adapters.BulkGasAdapter;
import com.softark.eddie.gasexpress.adapters.ServiceAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
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

public class BulkGasData {

    private Context context;
    private GEPreference preference;
    private RequestSingleton singleton;

    public BulkGasData(Context context) {
        this.context = context;
        preference = new GEPreference(context);
        singleton = new RequestSingleton(context);
    }

    public void getBulkGases(final RecyclerView recyclerView) {

        final ArrayList<BulkGas> gases = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_BULK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray gasArray = new JSONArray(response);
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
                        error.printStackTrace();
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
