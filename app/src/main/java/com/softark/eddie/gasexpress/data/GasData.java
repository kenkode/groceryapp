package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.adapters.GasAdapter;
import com.softark.eddie.gasexpress.models.RGas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GasData {

    private final Context context;

    public GasData(Context context) {
        this.context = context;
    }

    public void getGases(final int size, final ListView listView, final LinearLayout errorLinear, final ProgressBar loadPrice) {

        final ArrayList<RGas> rGases = new ArrayList<>();

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        Call<List<RGas>> retroGases = retrofitInterface.getGases(size);

        retroGases.enqueue(new Callback<List<RGas>>() {
            @Override
            public void onResponse(Call<List<RGas>> call, retrofit2.Response<List<RGas>> response) {
                List<RGas> gases = response.body();
                for (RGas rGas : gases) {
                    rGases.add(rGas);
                }
                if(rGases.size() <= 0) {
                    errorLinear.setVisibility(View.VISIBLE);
                }else {
                    errorLinear.setVisibility(View.GONE);
                }
                loadPrice.setVisibility(View.GONE);
                GasAdapter adapter = new GasAdapter(context, rGases);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<RGas>> call, Throwable t) {
                t.printStackTrace();
                errorLinear.setVisibility(View.VISIBLE);
                loadPrice.setVisibility(View.GONE);
                final Snackbar snackbar = Snackbar.make(listView, "Something went wrong!", Snackbar.LENGTH_INDEFINITE);
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
        });



//        final ArrayList<Gas> gases = new ArrayList<>();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_GASES,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            if(jsonArray.length() <= 0) {
//                                errorLinear.setVisibility(View.VISIBLE);
//                            }else {
//                                errorLinear.setVisibility(View.GONE);
//                            }
//                            loadPrice.setVisibility(View.GONE);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject object = jsonArray.getJSONObject(i);
//                                Gas gas = new Gas();
//                                gas.setLocation_id(object.getString("id"));
//                                gas.setName(object.getString("name"));
//                                gas.setSize(object.getInt("price"));
//                                gas.setPrice(object.getDouble("price"));
//                                gases.add(gas);
//                            }
//
//                            GasAdapter adapter = new GasAdapter(context, gases);
//                            listView.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        String message = "";
//                        if(error instanceof TimeoutError || error instanceof NetworkError) {
//                            message = "Please check your connection and try again.";
//                        }else if(error instanceof ServerError) {
//                            message = "Server experienced internal error. Please try again later.";
//                        }
//                        errorLinear.setVisibility(View.VISIBLE);
//                        loadPrice.setVisibility(View.GONE);
//                        final Snackbar snackbar = Snackbar.make(listView, message, Snackbar.LENGTH_INDEFINITE);
//                        snackbar.setAction("Retry", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                snackbar.dismiss();
//                                errorLinear.setVisibility(View.GONE);
//                                loadPrice.setVisibility(View.VISIBLE);
//                                getGases(size, listView, errorLinear, loadPrice);
//                            }
//                        });
//                        snackbar.show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                GEPreference preference = new GEPreference(context);
//                params.put(Constants.SIZE, String.valueOf(size));
//                params.put("user", preference.getUser().get(GEPreference.USER_ID));
//                return params;
//            }
//        };
//        requestSingleton.addToRequestQueue(stringRequest);
    }

}
