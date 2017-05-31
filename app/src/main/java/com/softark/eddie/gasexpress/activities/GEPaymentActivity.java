package com.softark.eddie.gasexpress.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.adapters.PaymentProcessAdapter;
import com.softark.eddie.gasexpress.helpers.GEPreference;

public class GEPaymentActivity extends AppCompatActivity {

    private GEPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gepayment);
        preference = new GEPreference(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.saf_listview);
        RadioGroup paymentGroup = (RadioGroup) findViewById(R.id.payment_option);

        for(int i = 0; i < paymentGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) paymentGroup.getChildAt(i);
            if(radioButton.getText().equals(preference.getPaymentOption())) {
                radioButton.setChecked(true);
                break;
            }
        }

        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                preference.setPaymentOption(radioButton.getText().toString());
            }
        });

        String[] process = getResources().getStringArray(R.array.mpesa_payment);

        PaymentProcessAdapter adapter = new PaymentProcessAdapter(this, process);

        recyclerView.setAdapter(adapter);

    }
}
