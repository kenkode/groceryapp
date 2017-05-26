package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.BulkGasAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.RBulkGas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BulkGasData {

    private final Context context;
    private final GEPreference preference;
    private final RequestSingleton singleton;

    public BulkGasData(Context context) {
        this.context = context;
        preference = new GEPreference(context);
        singleton = new RequestSingleton(context);
    }

    public void getBulkGases(final RecyclerView recyclerView, final LinearLayout errorLayout, final ProgressBar loader) {

        final ArrayList<RBulkGas> gases = new ArrayList<>();

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);
        Call<List<RBulkGas>> bulkGases = retrofitInterface.getBulkGases();
        bulkGases.enqueue(new Callback<List<RBulkGas>>() {
            @Override
            public void onResponse(Call<List<RBulkGas>> call, retrofit2.Response<List<RBulkGas>> response) {
                List<RBulkGas> bulkGasesList = response.body();
                for (RBulkGas rBulkGas :
                        bulkGasesList) {
                    gases.add(rBulkGas);
                }
                loader.setVisibility(View.GONE);
                if(gases.size() <= 0) {
                    errorLayout.setVisibility(View.VISIBLE);
                }else {
                    errorLayout.setVisibility(View.GONE);
                }
                BulkGasAdapter adapter = new BulkGasAdapter(context, gases);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<RBulkGas>> call, Throwable t) {
                loader.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                final Snackbar snackbar = Snackbar.make(errorLayout, "Oops!Something went wrong", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        errorLayout.setVisibility(View.GONE);
                        loader.setVisibility(View.VISIBLE);
                        getBulkGases(recyclerView, errorLayout, loader);
                    }
                });
                snackbar.show();
            }
        });
    }

}
