package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.DistributorAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Gas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 4/30/2017.
 */

public class SizeData {

    private RequestSingleton requestSingleton;
    private Context context;
    private DistributorAdapter adapter;
    private GEPreference preference;

    public SizeData(Context context) {
        this.context = context;
        requestSingleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void getSizes(final RecyclerView recyclerView, final ProgressBar progressBar) {
        final ArrayList<Gas> gases = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_SIZES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);

                            JSONArray jsonArray= new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Gas gas = new Gas();
                                gas.setId(object.getString("id"));
                                gas.setPrice(object.getDouble("price"));
                                gas.setSize(object.getInt("size"));
                                gases.add(gas);
                            }
                            adapter = new DistributorAdapter(gases, context);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
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

        requestSingleton.addToRequestQueue(stringRequest);
    }

}
