package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.RBulkGas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BulkGasData {

    private final Context context;
    private final GEPreference preference;

    public BulkGasData(Context context) {
        this.context = context;
        preference = new GEPreference(context);
    }

    public void getBulkPrice(final TextView textView, final Button button, final NumberPicker numberPicker) {

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);
        Call<Integer> bulkGases = retrofitInterface.getBulkPrice();
        bulkGases.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                int price = response.body();
                textView.setText(String.valueOf(price));
                button.setEnabled(true);
                numberPicker.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "Error fetching bulk price", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
