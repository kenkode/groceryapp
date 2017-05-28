package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.adapters.AccessoryAdapter;
import com.softark.eddie.gasexpress.adapters.ServiceAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.RAccessory;
import com.softark.eddie.gasexpress.models.RService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AccessoryServiceData {

    private final Context context;
    private final GEPreference preference;

    public AccessoryServiceData(Context context) {
        this.context = context;
        preference = new GEPreference(context);
    }

    public void getAccessories(final RecyclerView accessoriesRv, final LinearLayout errorLayout, final ProgressBar progressBar) {
        final ArrayList<RAccessory> accessories = new ArrayList<>();

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        final Call<List<RAccessory>> rAccessories = retrofitInterface.getAccessories();
        rAccessories.enqueue(new Callback<List<RAccessory>>() {
            @Override
            public void onResponse(Call<List<RAccessory>> call, retrofit2.Response<List<RAccessory>> response) {
                List<RAccessory> rAccess = response.body();
                for (RAccessory rAccessory : rAccess) {
                    accessories.add(rAccessory);
                }
                progressBar.setVisibility(View.GONE);
                Log.i("SIZE", accessories.size() + "");
                errorLayout.setVisibility(View.GONE);
                if(accessories.size() >= 1) {
                    errorLayout.setVisibility(View.GONE);
                }else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
                AccessoryAdapter accessoryAdapter = new AccessoryAdapter(context, accessories);
                accessoriesRv.setAdapter(accessoryAdapter);
            }

            @Override
            public void onFailure(Call<List<RAccessory>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                final Snackbar snackbar = Snackbar.make(errorLayout, "Oops! Something went wrong", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        errorLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getAccessories(accessoriesRv, errorLayout, progressBar);
                    }
                });
                snackbar.show();
            }
        });
    }

    public void getServices(final RecyclerView servicesRv, final LinearLayout errorLayout, final ProgressBar progressBar) {
        final ArrayList<RService> services = new ArrayList<>();

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        final Call<List<RService>> rServices = retrofitInterface.getServices();
        rServices.enqueue(new Callback<List<RService>>() {
            @Override
            public void onResponse(Call<List<RService>> call, retrofit2.Response<List<RService>> response) {
                List<RService> rAccess = response.body();
                for (RService rService: rAccess) {
                    services.add(rService);
                }
                progressBar.setVisibility(View.GONE);
                if(services.size() >= 1) {
                    errorLayout.setVisibility(View.GONE);
                }else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
                ServiceAdapter serviceAdapter = new ServiceAdapter(context, services);
                servicesRv.setAdapter(serviceAdapter);
            }

            @Override
            public void onFailure(Call<List<RService>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                final Snackbar snackbar = Snackbar.make(errorLayout, "Oops! Something went wrong", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        errorLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getServices(servicesRv, errorLayout, progressBar);
                    }
                });
                snackbar.show();
            }
        });
    }

}
