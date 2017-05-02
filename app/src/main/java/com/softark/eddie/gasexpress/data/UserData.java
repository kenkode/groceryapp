package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
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
import com.softark.eddie.gasexpress.helpers.GEPreference;

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

    public void authUser(final String phone) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.AUTH_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("RETR", response);
                            if(jsonObject.getString("status").equals("E")) {
                                JSONObject user = jsonObject.getJSONObject("user");
                                String id = user.getString("id");
                                String name = user.getString("name");
                                String phone = user.getString("phone");
                                String email = user.getString("email");
                                preference.setUser(id, name, phone, email);
                                Intent intent = new Intent(context, GasExpress.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else if(jsonObject.getString("status").equals("DNE")) {
                                Intent intent = new Intent(context, GERegisterActivity.class);
                                intent.putExtra("phone", phone);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
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
                params.put("phone", phone);
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }


}
