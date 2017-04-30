package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.PriceAdapter;
import com.softark.eddie.gasexpress.models.Gas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 4/16/2017.
 */

public class GasData {

    private RequestSingleton requestSingleton;
    private Context context;

    public GasData(Context context) {
        this.context = context;
        requestSingleton = new RequestSingleton(context);
    }

    public void getGases(final int size, final ListView listView) {

        final ArrayList<Gas> gases = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_GASES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Gas gas = new Gas();
                                gas.setId(object.getString("id"));
                                gas.setName(object.getString("name"));
                                gas.setSize(object.getInt("price"));
                                gas.setPrice(object.getDouble("price"));
                                gases.add(gas);
                            }

                            PriceAdapter adapter = new PriceAdapter(context, gases);
                            listView.setAdapter(adapter);

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
                params.put(Constants.SIZE, String.valueOf(size));
                return params;
            }
        };

        requestSingleton.addToRequestQueue(stringRequest);
    }

}
