package com.softark.eddie.gasexpress;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.softark.eddie.gasexpress.data.UserData;
import com.softark.eddie.gasexpress.models.Location;

import java.util.Calendar;

import static com.softark.eddie.gasexpress.Constants.LOCATION_ID;

public class GERegisterActivity extends AppCompatActivity {

    private EditText name, email, phone, location, birthday, description;
    private ImageButton selectLocation, selectBirthday;
    private Button registerButton;
    private UserData userData;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geregister);

        userData = new UserData(this);
        userLocation = null;

        name = (EditText) findViewById(R.id.register_customer_name);
        phone = (EditText) findViewById(R.id.register_customer_phone);
        email = (EditText) findViewById(R.id.register_customer_email);
        location = (EditText) findViewById(R.id.register_customer_location);
        birthday = (EditText) findViewById(R.id.register_customer_birthday);
        selectLocation = (ImageButton) findViewById(R.id.select_location);
        description = (EditText) findViewById(R.id.location_description);
        selectBirthday = (ImageButton) findViewById(R.id.select_birthday);
        registerButton = (Button) findViewById(R.id.register);

        selectBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(GERegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "-" + month + "-" + year;
                        birthday.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle("Date of birth");
                dialog.show();
            }
        });

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GERegisterActivity.this, GELocation.class);
                startActivityForResult(intent, LOCATION_ID);
                getIntent().putExtra("name", name.getText().toString().trim());
                getIntent().putExtra("email", email.getText().toString().trim());
                getIntent().putExtra("phone", phone.getText().toString().trim());
                getIntent().putExtra("birthday", birthday.getText().toString().trim());
                getIntent().putExtra("description", description.getText().toString().trim());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()) {
                    String nm = name.getText().toString().trim();
                    String eml = email.getText().toString().trim();
                    String phn = phone.getText().toString().trim();
                    String birthday = location.getText().toString().trim();
                    String desc = description.getText().toString().trim();
                    userData.addUser(nm, eml, phn, birthday, desc, userLocation);
                }else {
                    Toast.makeText(GERegisterActivity.this, "Please provide all the details", Toast.LENGTH_LONG).show();
                }

            }
        });

        Intent intent = getIntent();
        if(intent != null) {
            phone.setText(intent.getStringExtra("phone"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == LOCATION_ID) {
                if(data != null) {
                    Location loc = data.getParcelableExtra("location");
                    userLocation = loc;
                    location.setText(loc.getAddress());
                }
                if(getIntent() != null) {
                    name.setText(getIntent().getStringExtra("name"));
                    email.setText(getIntent().getStringExtra("email"));
                    phone.setText(getIntent().getStringExtra("phone"));
                    birthday.setText(getIntent().getStringExtra("birthday"));
                    description.setText(getIntent().getStringExtra("description"));
                }
            }
        }
    }

    public boolean isValid() {
        String nm = name.getText().toString().trim();
        String eml = email.getText().toString().trim();
        String phn = phone.getText().toString().trim();
        String bd = birthday.getText().toString().trim();
        String desc= description.getText().toString().trim();

        return !(nm.isEmpty() || eml.isEmpty() || phn.isEmpty() || desc.isEmpty() || bd.isEmpty() || userLocation == null);
    }
}
