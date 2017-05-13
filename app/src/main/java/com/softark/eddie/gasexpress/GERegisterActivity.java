package com.softark.eddie.gasexpress;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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

    private final int EMPTY_NAME = 1;
    private final int INVALID_EMAIL = 3;
    private final int EMPTY_EMAIL = 2;
    private final int EMPTY_PHONE = 4;
    private final int INVALID_PHONE = 5;
    private final int EMPTY_BD = 6;
    private final int EMPTY_LOCATION = 7;
    private final int EMPTY_DESC = 9;
    private final int SHORT_DESC = 8;

    private final String[] MESSAGES = new String[] {
            "Please provide your name",
            "Please provide your email",
            "Invalid email",
            "Please provide your phone number",
            "Invalid phone number",
            "Please provide your birthday",
            "Please provide your location",
            "Short description",
            "Give a brief description of your location"
    };

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
                        String date = year + "-" + month + "-" + dayOfMonth;
                        birthday.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
                if(isValid() == 0) {
                    String nm = name.getText().toString().trim();
                    String eml = email.getText().toString().trim();
                    String phn = phone.getText().toString().trim();
                    String birthday = location.getText().toString().trim();
                    String desc = description.getText().toString().trim();
                    ProgressDialog progressDialog = new ProgressDialog(GERegisterActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Registering...");
                    userData.addUser(nm, eml, phn, birthday, email, desc, userLocation, progressDialog);
                }else {
                    Toast.makeText(GERegisterActivity.this, MESSAGES[isValid()-1], Toast.LENGTH_LONG).show();
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

    private int isValid() {
        String nm = name.getText().toString().trim();
        String eml = email.getText().toString().trim();
        String phn = phone.getText().toString().trim();
        String bd = birthday.getText().toString().trim();
        String desc= description.getText().toString().trim();

        if(nm.isEmpty()) {
            return EMPTY_NAME;
        }else if(eml.isEmpty()) {
            return EMPTY_EMAIL;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(eml).matches()) {
            return INVALID_EMAIL;
        }else if(phn.isEmpty()) {
            return EMPTY_PHONE;
        }else if(phn.matches("^[+][0-9]{10}$")) {
            return INVALID_PHONE;
        }else if(bd.isEmpty()) {
            return EMPTY_BD;
        }else if(userLocation == null) {
            return EMPTY_LOCATION;
        }else if(desc.isEmpty()) {
            return EMPTY_DESC;
        }else if(desc.length() < 15) {
            return SHORT_DESC;
        }

        return 0;
    }
}
