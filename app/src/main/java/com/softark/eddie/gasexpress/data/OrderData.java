package com.softark.eddie.gasexpress.data;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.activities.GEHistory;
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
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class OrderData {

    private final Context context;
    private final RequestSingleton singleton;
    private final GEPreference preference;

    public OrderData(Context context) {
        this.context = context;
        singleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void placeOrder(String payment, String cartItems, final ProgressDialog progressDialog) {
        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);
        Call<ResponseBody> makeOrder = retrofitInterface.placeOrder(cartItems,
                preference.getUser().get(GEPreference.USER_ID), Checkout.getLocation().getId(), payment);
        makeOrder.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
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

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    public void getOrders(final RecyclerView recyclerView, final LinearLayout historyState, final ProgressBar progressBar) {
        final ArrayList<OrderHistory> orderHistories = new ArrayList<>();

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);
        Call<List<OrderHistory>> orders = retrofitInterface.getOrders(preference.getUser().get(GEPreference.USER_ID));

        orders.enqueue(new Callback<List<OrderHistory>>() {
            @Override
            public void onResponse(Call<List<OrderHistory>> call, retrofit2.Response<List<OrderHistory>> response) {
                List<OrderHistory> histories = response.body();
                for (OrderHistory orderHistory :
                        histories) {
                    orderHistories.add(orderHistory);
                }
                progressBar.setVisibility(View.GONE);
                if(orderHistories.size() > 0) {
                    historyState.setVisibility(View.GONE);
                }else {
                    historyState.setVisibility(View.VISIBLE);
                }
                HistoryAdapter adapter = new HistoryAdapter(context, orderHistories);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<OrderHistory>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                final Snackbar snackbar = Snackbar.make(recyclerView, "Something went wrong!", Snackbar.LENGTH_INDEFINITE);
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
        });

    }

    public void getOrderItems(final String id, final RecyclerView listView) {

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        final Call<List<OrderItem>> orderItems = retrofitInterface.getOrderItems(preference.getUser().get(GEPreference.USER_ID),
                id);

        orderItems.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, retrofit2.Response<List<OrderItem>> response) {
                ArrayList<OrderItem> orderItemArrayList = new ArrayList<OrderItem>();
                for (OrderItem orderItem :
                        response.body()) {
                    orderItemArrayList.add(orderItem);
                }
                ItemAdapter itemAdapter = new ItemAdapter(context, orderItemArrayList);
                listView.setAdapter(itemAdapter);
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
