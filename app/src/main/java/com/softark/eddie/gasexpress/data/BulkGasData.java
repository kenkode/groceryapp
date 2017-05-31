package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.helpers.GEPreference;

import retrofit2.Call;
import retrofit2.Callback;

public class BulkGasData {

    public void getBulkPrice(final TextView textView1, final TextView textView, final Button button, final NumberPicker numberPicker) {

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);
        Call<Integer> bulkGases = retrofitInterface.getBulkPrice();
        bulkGases.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                int price = response.body();
                textView.setText(String.valueOf(price));
                button.setEnabled(true);
                numberPicker.setEnabled(true);
                int ttlPrice = price * numberPicker.getValue();
                textView1.setText(String.valueOf(ttlPrice));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(textView, "Error fetching bulk price", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBulkPrice(textView1, textView, button, numberPicker);
                    }
                });
            }
        });
    }

}
