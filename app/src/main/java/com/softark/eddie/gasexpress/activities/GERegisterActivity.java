package com.softark.eddie.gasexpress.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softark.eddie.gasexpress.Constants;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.Singleton.RequestSingleton;
import com.softark.eddie.gasexpress.data.UserData;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.softark.eddie.gasexpress.Constants.LOCATION_ID;

public class GERegisterActivity extends AppCompatActivity {

    private EditText name, email, phone, location, birthday, description;
    private ImageButton selectLocation, selectBirthday;
    private Button registerButton;
    private UserData userData;
    private Location userLocation;
    private GEPreference preference;
    private RequestSingleton singleton;

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
        preference = new GEPreference(this);
        singleton = new RequestSingleton(this);;

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
                    final String phn = phone.getText().toString().trim();
                    String bd = birthday.getText().toString().trim();
                    String desc = description.getText().toString().trim();
                    final ProgressDialog progressDialog = new ProgressDialog(GERegisterActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Registering...");
                    addUser(nm, eml, phn, bd, email, desc, userLocation, progressDialog);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void addUser(final String name, final String email, final String phone, final String birthday,final View view, final String description, final Location location, final ProgressDialog progressDialog) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ADD_USER", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            processResults(jsonObject, phone);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        String message = "";
                        if(error instanceof TimeoutError || error instanceof NetworkError) {
                            message = "Server took long to respond. Please try again.";
                        }else if(error instanceof ServerError) {
                            message = "Server experienced internal error. Please try again later.";
                        }
                        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressDialog.show();
                                addUser(name, email, phone, birthday, view, description, location, progressDialog);
                            }
                        });
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("birthday", birthday);
                params.put("description", description);
                params.put("phone", phone);
                params.put("address", location.getAddress());
                params.put("lat", String.valueOf(location.getLat()));
                params.put("lng", String.valueOf(location.getLng()));
                Log.i("PARAMS", params.toString());
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    private void processResults(JSONObject jsonObject, String phone) {
        try {
            if(jsonObject.getString("status").equals("E")) {
                JSONObject user = jsonObject.getJSONObject("user");
                String id = user.getString("id");
                String name = user.getString("name");
                String phn = user.getString("phone");
                String email = user.getString("email");
                preference.setUser(id, name, phn, email);
                Intent intent = new Intent(GERegisterActivity.this, GasExpress.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else if(jsonObject.getString("status").equals("EE")) {
                Toast.makeText(GERegisterActivity.this, "Email exists", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
