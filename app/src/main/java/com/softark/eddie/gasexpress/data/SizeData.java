package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.adapters.DistributorAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import retrofit2.Call;
import retrofit2.Callback;

public class SizeData {

    private final Context context;

    public SizeData(Context context) {
        this.context = context;
    }

    public void getSizes(final LinearLayout errorLayout, final RecyclerView recyclerView, final ProgressBar progressBar) {

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        //final Call<int[]> sizes = retrofitInterface.getSizes();

        final Call<String[]> categories = retrofitInterface.getCategories();

        categories.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, retrofit2.Response<String[]> response) {
                String[] names = response.body();
                progressBar.setVisibility(View.GONE);
                if(errorLayout.getVisibility() == View.VISIBLE) {
                  errorLayout.setVisibility(View.GONE);
                }
                if(names.length >= 1) {
                    DistributorAdapter adapter = new DistributorAdapter(names, context);
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(context, "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                if(errorLayout.getVisibility() == View.GONE) {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
