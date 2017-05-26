package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.adapters.DistributorAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.RGas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SizeData {

    private final RequestSingleton requestSingleton;
    private final Context context;
    private DistributorAdapter adapter;
    private final GEPreference preference;

    public SizeData(Context context) {
        this.context = context;
        requestSingleton = new RequestSingleton(context);
        preference = new GEPreference(context);
    }

    public void getSizes(final LinearLayout errorLayout, final RecyclerView recyclerView, final ProgressBar progressBar) {
        final ArrayList<RGas> rGases = new ArrayList<>();

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        Call<List<RGas>> retroGases = retrofitInterface.getGases(0);

        retroGases.enqueue(new Callback<List<RGas>>() {
            @Override
            public void onResponse(Call<List<RGas>> call, retrofit2.Response<List<RGas>> response) {
                List<RGas> gases = response.body();
                for (RGas rGas : gases) {
                    rGases.add(rGas);
                }
                progressBar.setVisibility(View.GONE);
                if(errorLayout.getVisibility() == View.VISIBLE) {
                  errorLayout.setVisibility(View.GONE);
                }
                DistributorAdapter adapter = new DistributorAdapter(rGases, context);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<RGas>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if(errorLayout.getVisibility() == View.GONE) {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
