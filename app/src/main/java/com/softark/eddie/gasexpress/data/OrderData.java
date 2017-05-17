package com.softark.eddie.gasexpress.data;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.softark.eddie.gasexpress.activities.GEHistory;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.HistoryAdapter;
import com.softark.eddie.gasexpress.adapters.ItemAdapter;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.Checkout;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.OrderHistory;
import com.softark.eddie.gasexpress.models.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderData {

    private final Context context;
    private final RequestSingleton singleton;
    private final GEPreference preference;

    public OrderData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void placeOrder(final String cartItems, final ProgressDialog progressDialog) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Cart.clearCart();
                        progressDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.checkout_success);
                        dialog.setCancelable(false);
                        Button button = (Button) dialog.findViewById(R.id.yes);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, GEHistory.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        String message = "";
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "Error connecting to the server. Please try again later.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                params.put("location", Checkout.getLocation().getId());
                params.put("json", cartItems);
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    public void getOrders(final RecyclerView recyclerView, final LinearLayout historyState, final ProgressBar progressBar) {
        final ArrayList<OrderHistory> orderHistories = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            progressBar.setVisibility(View.GONE);
                            if(jsonArray.length() > 0) {
                                historyState.setVisibility(View.GONE);
                            }else {
                                historyState.setVisibility(View.VISIBLE);
                            }
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
                        progressBar.setVisibility(View.GONE);
                        String message = "";
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
                                progressBar.setVisibility(View.VISIBLE);
                                getOrders(recyclerView, historyState, progressBar);
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

    public void getOrderItems(final String id, final RecyclerView listView) {
        final ArrayList<OrderItem> orderItems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_ORDER_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                OrderItem orderItem = new OrderItem();
                                orderItem.setName(object.getString("name"));
                                orderItem.setId(object.getString("id"));
                                orderItem.setPrice(object.getDouble("price"));
                                orderItem.setQuantity(object.getInt("qty"));
                                orderItems.add(orderItem);
                            }
                            ItemAdapter itemAdapter = new ItemAdapter(context, orderItems);
                            listView.setAdapter(itemAdapter);

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
                            message = "No internet connection. Please try again later.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", preference.getUser().get(GEPreference.USER_ID));
                params.put("id", id);
                return params;
            }

        };
        singleton.addToRequestQueue(stringRequest);
    }

}
