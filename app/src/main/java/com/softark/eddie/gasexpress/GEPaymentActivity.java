package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.helpers.GEPreference;

public class GEPaymentActivity extends AppCompatActivity {

    private ListView listView;
    private RadioGroup paymentGroup;
    private GEPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gepayment);
        preference = new GEPreference(this);

        listView = (ListView) findViewById(R.id.saf_listview);
        paymentGroup = (RadioGroup) findViewById(R.id.payment_option);

        for(int i = 0;i < paymentGroup.getChildCount();i++) {
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

        listView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.listview_textview,
                getResources().getStringArray(R.array.mpesa_payment)));

    }
}
