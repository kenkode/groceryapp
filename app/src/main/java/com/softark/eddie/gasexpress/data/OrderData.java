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
import com.softark.eddie.gasexpress.helpers.OrderKey;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;
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
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

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

    public void placeOrder(final String cartItems) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Cart.clearCart();
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
                params.put("json", cartItems);
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
                                JSONObject o = jsonArray.getJSONObject(i);
                                JSONObject orders = o.getJSONObject("orders");
                                OrderHistory orderHistory = new OrderHistory();
                                orderHistory.setDate(orders.getString("created_at"));
                                orderHistory.setId(orders.getString("order_id"));
                                orderHistory.setPrice(orders.getDouble("price"));
                                orderHistory.setOrderType(o.getString("type"));

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
