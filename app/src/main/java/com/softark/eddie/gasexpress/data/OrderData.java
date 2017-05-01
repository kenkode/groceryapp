package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.GELocation;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Distributor;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eddie on 4/30/2017.
 */

public class OrderData {

    private Context context;
    private RequestSingleton singleton;
    private GEPreference preference;
    final ArrayList<Distributor> distributorArrayList = new ArrayList<>();
    final ArrayList<Location> locationArrayList = new ArrayList<>();

    private String strDistributor;
    private String strSize;
    private String strLocation;

    public OrderData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void populateSpinners(final Spinner spnDistributor, final Spinner spnSize, final Spinner spnLocation, final TextView price) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.POPULATE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray distributors = jsonObject.getJSONArray("distributors");
                            JSONArray locations = jsonObject.getJSONArray("locations");
                            final List<String> lstDistributors = new ArrayList<>();
                            final List<String> lstLocation = new ArrayList<>();

                            for (int i = 0; i < distributors.length(); i++) {
                                JSONObject object = distributors.getJSONObject(i);
                                Distributor distributor = new Distributor();
                                distributor.setId(object.getString("id"));
                                distributor.setName(object.getString("name"));
                                distributorArrayList.add(distributor);
                                lstDistributors.add(object.getString("name"));
                            }

                            for (int i = 0; i < locations.length(); i++) {
                                JSONObject object = locations.getJSONObject(i);
                                Location location = new Location();
                                Log.i("LCT", object.getString("address"));
                                location.setAddress(object.getString("address"));
                                location.setLat(object.getDouble("lat"));
                                location.setLng(object.getDouble("lng"));
                                location.setId(object.getString("location_id"));
                                locationArrayList.add(location);
                                lstLocation.add(object.getString("address"));
                            }

                            spnDistributor.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                                    lstDistributors));

                            spnLocation.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                                    lstLocation));

                            spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    strLocation = lstLocation.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            spnDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Distributor distributor = distributorArrayList.get(position);
                                    price.setText("Kes 0");
                                    strSize = "";
                                    populateSizes(spnSize, price, distributor.getId());
                                    strDistributor = distributor.getId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

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

    public void populateSizes(final Spinner spnSize, final TextView price, final String distributor) {

        final ArrayList<Gas> gases = new ArrayList<>();
        final List<String> sizes = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_SIZES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray= new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Gas gas = new Gas();
                                gas.setId(object.getString("id"));
                                gas.setPrice(object.getDouble("price"));
                                gas.setSize(object.getInt("size"));
                                gas.setName(object.getString("name"));
                                gases.add(gas);
                                sizes.add(object.getString("size"));
                            }

                            spnSize.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                                    sizes));

                            spnSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    strSize = String.valueOf(sizes.get(position));
                                    Gas gas = gases.get(position);
                                    price.setText("Kes " + gas.getPrice());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

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
                params.put("distributor", distributor);
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void placeOrder() {
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
                params.put("distributor", strDistributor);
                params.put("size", strSize);
                params.put("lat", "1.2821");
                params.put("lng", "36.8781");
                params.put("location", strLocation);
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

}
