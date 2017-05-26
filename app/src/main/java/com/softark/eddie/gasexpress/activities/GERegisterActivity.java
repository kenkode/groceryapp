package com.softark.eddie.gasexpress.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Location;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.softark.eddie.gasexpress.Constants.LOCATION_ID;

public class GERegisterActivity extends AppCompatActivity {

    private EditText fname, lname, email, location, birthday, description;
    private IntlPhoneInput phone;
    private Location userLocation;
    private GEPreference preference;
    private RequestSingleton singleton;

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
        singleton = new RequestSingleton(this);

        userLocation = null;

        fname = (EditText) findViewById(R.id.register_customer_first_name);
        lname = (EditText) findViewById(R.id.register_customer_last_name);
        phone = (IntlPhoneInput) findViewById(R.id.register_customer_phone);
        phone.setEmptyDefault("KE");
        email = (EditText) findViewById(R.id.register_customer_email);
        location = (EditText) findViewById(R.id.register_customer_location);
        birthday = (EditText) findViewById(R.id.register_customer_birthday);
        ImageButton selectLocation = (ImageButton) findViewById(R.id.select_location);
        description = (EditText) findViewById(R.id.location_description);
        ImageButton selectBirthday = (ImageButton) findViewById(R.id.select_birthday);
        Button registerButton = (Button) findViewById(R.id.register);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GERegisterActivity.this, GELocation.class);
                startActivityForResult(intent, LOCATION_ID);
                getIntent().putExtra("fname", fname.getText().toString().trim());
                getIntent().putExtra("lname", lname.getText().toString().trim());
                getIntent().putExtra("email", email.getText().toString().trim());
                getIntent().putExtra("phone", phone.getNumber().trim());
                getIntent().putExtra("birthday", birthday.getText().toString().trim());
                getIntent().putExtra("description", description.getText().toString().trim());
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
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
                getIntent().putExtra("fname", fname.getText().toString().trim());
                getIntent().putExtra("lname", lname.getText().toString().trim());
                getIntent().putExtra("email", email.getText().toString().trim());
                getIntent().putExtra("phone", phone.getNumber().trim());
                getIntent().putExtra("birthday", birthday.getText().toString().trim());
                getIntent().putExtra("description", description.getText().toString().trim());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid() == 0) {
                    String fnm = fname.getText().toString().trim();
                    String lnm = lname.getText().toString().trim();
                    String eml = email.getText().toString().trim();
                    final String phn = String.valueOf(phone.getPhoneNumber().getCountryCode())
                            .concat(String.valueOf(phone.getPhoneNumber().getNationalNumber())).trim();
                    String bd = birthday.getText().toString().trim();
                    String desc = description.getText().toString().trim();
                    final ProgressDialog progressDialog = new ProgressDialog(GERegisterActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();
                    addUser(fnm, lnm, eml, phn, bd, email, desc, userLocation, progressDialog);
                }else {
                    Toast.makeText(GERegisterActivity.this, MESSAGES[isValid()-1], Toast.LENGTH_LONG).show();
                }

            }
        });

        Intent intent = getIntent();
        if(intent != null) {
            phone.setNumber(intent.getStringExtra("phone"));
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
                    fname.setText(getIntent().getStringExtra("fname"));
                    lname.setText(getIntent().getStringExtra("lname"));
                    email.setText(getIntent().getStringExtra("email"));
                    phone.setNumber(getIntent().getStringExtra("phone"));
                    birthday.setText(getIntent().getStringExtra("birthday"));
                    description.setText(getIntent().getStringExtra("description"));
                }
            }
        }
    }

    private int isValid() {
        String fnm = fname.getText().toString().trim();
        String lnm = lname.getText().toString().trim();
        String eml = email.getText().toString().trim();
        String phn = phone.getNumber().trim();
        String bd = birthday.getText().toString().trim();
        String desc= description.getText().toString().trim();

        if(fnm.isEmpty() || lnm.isEmpty()) {
            return 1;
        }else if(eml.isEmpty()) {
            return 2;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(eml).matches()) {
            return 3;
        }else if(phn.isEmpty()) {
            return 4;
        }else if(!phone.isValid()) {
            return 5;
        }else if(bd.isEmpty()) {
            return 6;
        }else if(userLocation == null) {
            return 7;
        }else if(desc.isEmpty()) {
            return 9;
        }else if(desc.length() < 15) {
            return 8;
        }

        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void addUser(final String fname, final String lname, final String email, final String phone, final String birthday, final View view, final String description, final Location location, final ProgressDialog progressDialog) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getString("status").equals("EE")) {
                                confirmPin(jsonObject, progressDialog);
                            }else {
                                Toast.makeText(GERegisterActivity.this, "Email exists", Toast.LENGTH_LONG).show();
                            }
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
                                addUser(fname, lname, email, phone, birthday, view, description, location, progressDialog);
                            }
                        });
                        snackbar.show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("email", email);
                params.put("birthday", birthday);
                params.put("description", description);
                params.put("phone", phone);
                params.put("address", location.getAddress());
                params.put("lat", String.valueOf(location.getLat()));
                params.put("lng", String.valueOf(location.getLng()));
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    private void confirmPin(final JSONObject jsonObject, final ProgressDialog dialog) {
        try {
            final String pin = jsonObject.getString("pin");
            dialog.dismiss();
            final Dialog dialog1 = new Dialog(GERegisterActivity.this);
            dialog1.setCancelable(false);
            dialog1.setContentView(R.layout.pin_input_dialog);
            Button cancel = (Button) dialog1.findViewById(R.id.cancel);
            Button submit = (Button) dialog1.findViewById(R.id.ok);
            final EditText pinText = (EditText) dialog1.findViewById(R.id.pin_edit);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pinText.getText().toString().isEmpty()) {
                        Toast.makeText(GERegisterActivity.this, "Insert pin.", Toast.LENGTH_LONG).show();
                    }else {
                        if(pinText.getText().toString().equals(pin.trim())) {
                            processResults(jsonObject);
                        }else {
                            Toast.makeText(GERegisterActivity.this, "Incorrect pin.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            dialog1.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResults(JSONObject jsonObject) {
        try {
            if(jsonObject.getString("status").equals("E")) {
                JSONObject user = jsonObject.getJSONObject("user");
                String id = user.getString("id");
                String fnm = user.getString("fname");
                String lnm = user.getString("lname");
                String phn = user.getString("phone");
                String email = user.getString("email");
                preference.setUser(id, fnm, lnm, phn, email);
                Intent intent = new Intent(GERegisterActivity.this, GasExpress.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
