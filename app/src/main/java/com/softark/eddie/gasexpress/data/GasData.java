package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.softark.eddie.gasexpress.adapters.GasAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
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

    public void getGases(final int size, final ListView listView, final LinearLayout errorLinear, final ProgressBar loadPrice) {

        final ArrayList<Gas> gases = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_GASES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() <= 0) {
                                errorLinear.setVisibility(View.VISIBLE);
                            }else {
                                errorLinear.setVisibility(View.GONE);
                            }
                            loadPrice.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Gas gas = new Gas();
                                gas.setId(object.getString("id"));
                                gas.setName(object.getString("name"));
                                gas.setSize(object.getInt("price"));
                                gas.setPrice(object.getDouble("price"));
                                gases.add(gas);
                            }

                            GasAdapter adapter = new GasAdapter(context, gases);
                            listView.setAdapter(adapter);

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
                            message = "Please check your connection and try again.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }else if (error instanceof NetworkError) {
                            message = "Network error. Please try again later.";
                        }
                        errorLinear.setVisibility(View.VISIBLE);
                        loadPrice.setVisibility(View.GONE);
                        final Snackbar snackbar = Snackbar.make(listView, message, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                                errorLinear.setVisibility(View.GONE);
                                loadPrice.setVisibility(View.VISIBLE);
                                getGases(size, listView, errorLinear, loadPrice);
                            }
                        });
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.SIZE, String.valueOf(size));
                GEPreference preference = new GEPreference(context);
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                return params;
            }
        };
        requestSingleton.addToRequestQueue(stringRequest);
    }

}
