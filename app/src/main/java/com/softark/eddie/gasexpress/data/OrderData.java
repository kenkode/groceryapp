package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.softark.eddie.gasexpress.adapters.HistoryAdapter;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.Checkout;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.Distributor;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Location;
import com.softark.eddie.gasexpress.models.OrderHistory;
import com.softark.eddie.gasexpress.models.Service;

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

    public OrderData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void requestOrderKey() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ORDER_KEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty()) {
                            preference.setOrderKey(response);
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

    public void placeGasOrder(final int orderType, final Gas gas) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        if(response.equals("1")) {
//                            ArrayList<Gas> gases = (ArrayList<Gas>) Cart.getInstance().getCart().get(Cart.GASES);
//                            for (Gas gas :  gases){
//                                if(gas.getId().equals(gas.getId())) {
//                                    Cart.removeProduct(gases.indexOf(gas));
//                                }
//                            }
//                        }
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

                Location location = Checkout.getLocation();
                params.put("id", gas.getId());
                params.put("qty", String.valueOf(gas.getQuantity()));
                params.put("location", location.getId());
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                params.put("type", String.valueOf(orderType));
                params.put("key", preference.getOrderKey());

                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void placeBulkGasOrder(final int orderType, final BulkGas item) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        if(response.equals("1")) {
//                            ArrayList<Accessory> accessories = (ArrayList<Accessory>) Cart.getInstance().getCart().get(Cart.ACCESSORIES);
//                            for (Accessory accessory :  accessories){
//                                if(accessory.getId().equals(item.getId())) {
//                                    Cart.removeProduct(accessories.indexOf(accessory));
//                                }
//                            }
//                        }
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
                Location location = Checkout.getLocation();
                params.put("id", item.getId());
                params.put("qty", String.valueOf(item.getQuantity()));
                params.put("location", location.getId());
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                params.put("type", String.valueOf(orderType));
                params.put("key", preference.getOrderKey());
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void placeServiceOrder(final int orderType, final Service item) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        if(response.equals("1")) {
//                            ArrayList<Service> services = (ArrayList<Service>) Cart.getInstance().getCart().get(Cart.SERVICES);
//                            for (Service service :  services){
//                                if(service.getId().equals(item.getId())) {
//                                    Cart.removeProduct(services.indexOf(service));
//                                }
//                            }
//                        }
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
                Location location = Checkout.getLocation();
                params.put("id", item.getId());
                params.put("location", location.getId());
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                params.put("type", String.valueOf(orderType));
                params.put("key", preference.getOrderKey());
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void placeOrder(final int orderType, final Accessory item) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        if(response.equals("1")) {
//                            ArrayList<Accessory> accessories = (ArrayList<Accessory>) Cart.getInstance().getCart().get(Cart.ACCESSORIES);
//                            for (Accessory accessory :  accessories){
//                                if(accessory.getId().equals(item.getId())) {
//                                    Cart.removeProduct(accessories.indexOf(accessory));
//                                }
//                            }
//                        }
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
                Location location = Checkout.getLocation();
                params.put("id", item.getId());
                params.put("qty", String.valueOf(item.getQuantity()));
                params.put("location", location.getId());
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                params.put("type", String.valueOf(orderType));
                params.put("key", preference.getOrderKey());
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void getOrders(final RecyclerView recyclerView) {
        final ArrayList<OrderHistory> orderHistories = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                OrderHistory orderHistory = new OrderHistory();
                                orderHistory.setDate(object.getString("created_at"));
                                orderHistory.setId(object.getString("order_id"));
                                orderHistory.setPrice(object.getDouble("price"));
                                orderHistories.add(orderHistory);
                            }

                            HistoryAdapter adapter = new HistoryAdapter(context, orderHistories);
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
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                return params;
            }

        };
        singleton.addToRequestQueue(stringRequest);
    }

}
