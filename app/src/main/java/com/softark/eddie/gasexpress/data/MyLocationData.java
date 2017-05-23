package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.activities.GELocation;
import com.softark.eddie.gasexpress.activities.GEMyLocationActivity;
import com.softark.eddie.gasexpress.adapters.LocationAdapter;
import com.softark.eddie.gasexpress.helpers.Checkout;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLocationData {

    private final Context context;
    private final RequestSingleton singleton;
    private final GEPreference preference;
    private static LocationAdapter adapter;
    private static ArrayList<Location> locations;

    public MyLocationData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void getLocation(final RecyclerView recyclerView,
                            final Spinner spinner,
                            final LinearLayout errorLocation,
                            final ProgressBar loader, final Button checkout) {
        locations = new ArrayList<>();
        final List<String> list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_MY_LOCATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(errorLocation != null) {
                                errorLocation.setVisibility(View.GONE);
                            }
                            if(loader != null) {
                                loader.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Location location = new Location();
                                location.setLng(object.getDouble("lng"));
                                location.setLat(object.getDouble("lat"));
                                location.setAddress(object.getString("address"));
                                location.setType(object.getInt("type"));
                                location.setId(object.getString("location_id"));
                                list.add(object.getString("address"));
                                locations.add(location);
                            }

                            if(recyclerView != null) {
                                adapter = new LocationAdapter(context, locations);
                                recyclerView.setAdapter(adapter);
                            }

                            if(spinner != null) {
                                spinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom, list));
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Location location = locations.get(position);
                                        Checkout.setLocation(location);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                            if(checkout != null && list.size() > 0) {
                                checkout.setEnabled(true);
                            }else {
                                if(list.size() < 1) {
                                    if(spinner != null) {
                                        final Snackbar snackbar = Snackbar.make(spinner, "Add a delivery location", Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setAction("Add", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                snackbar.dismiss();
                                                Intent intent = new Intent(context, GEMyLocationActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);
                                            }
                                        });
                                        snackbar.show();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        String message = "";
                        if(errorLocation != null) {
                            errorLocation.setVisibility(View.VISIBLE);
                        }
                        if(loader != null) {
                            loader.setVisibility(View.GONE);
                        }
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "No internet connection. Please try again later.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        final Snackbar snackbar = Snackbar.make(recyclerView, message, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                                if(errorLocation != null) {
                                    errorLocation.setVisibility(View.GONE);
                                }
                                if(loader != null) {
                                    loader.setVisibility(View.VISIBLE);
                                }
                                getLocation(recyclerView, spinner, errorLocation, loader, checkout);
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

    public void addLocation(final Location location) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        location.setId(response);
                        Log.i("OO", response);
                        locations.add(location);
                        adapter.notifyDataSetChanged();
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
                params.put("lng", String.valueOf(location.getLng()));
                params.put("lat", String.valueOf(location.getLat()));
                params.put("address", location.getAddress());
                params.put("type", String.valueOf(location.getType()));
                params.put("desc", String.valueOf(location.getDescription()));
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void disableLocation(final String id, final ImageButton button) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DISABLE_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Snackbar snackbar = Snackbar.make(button, response, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "Error connecting to the internet. Please try again later.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        final Snackbar snackbar = Snackbar.make(button, message, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                                disableLocation(id, button);
                            }
                        });
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("location", id);
                GEPreference preference = new GEPreference(context);
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

}
