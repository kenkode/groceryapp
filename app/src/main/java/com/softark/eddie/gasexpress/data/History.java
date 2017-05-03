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
import com.softark.eddie.gasexpress.adapters.HistoryAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 4/17/2017.
 */

public class History {

    private Context context;
    private RequestSingleton singleton;

    public History(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
    }

    public ArrayList<com.softark.eddie.gasexpress.models.History> getHistory(final RecyclerView recyclerView) {
        final ArrayList<com.softark.eddie.gasexpress.models.History> histories = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("SMT", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                com.softark.eddie.gasexpress.models.History h = new com.softark.eddie.gasexpress.models.History();
                                h.setDate(jsonObject.getString("created_at"));
                                h.setType(jsonObject.getString("name"));
                                h.setPrice(jsonObject.getString("price"));
                                h.setSize(jsonObject.getInt("size"));
                                h.setTime(jsonObject.getString("created_at"));
                                histories.add(h);
                            }

                            HistoryAdapter adapter = new HistoryAdapter(context, histories);
                            recyclerView.setAdapter(adapter);

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
                GEPreference preference = new GEPreference(context);
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                return params;
            }
        };

        singleton.addToRequestQueue(stringRequest);

        return histories;
    }

}
