package com.softark.eddie.gasexpress.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.helpers.Internet;
import com.softark.eddie.gasexpress.models.Location;

import static com.softark.eddie.gasexpress.Constants.LOCATION_ID;
public class GEMyLocationActivity extends AppCompatActivity {

    private FloatingActionButton addLocation;
    private LinearLayout emptyLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gemy_location);

        addLocation = (FloatingActionButton) findViewById(R.id.add_location);
        LinearLayout errorLocation = (LinearLayout) findViewById(R.id.error_location_layout);
        errorLocation.setVisibility(View.GONE);

        emptyLocation = (LinearLayout) findViewById(R.id.no_location_layout);
        emptyLocation.setVisibility(View.GONE);


        ProgressBar locationLoader = (ProgressBar) findViewById(R.id.location_loader);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Internet.isConnected()) {
                    Intent intent = new Intent(GEMyLocationActivity.this, GELocation.class);
                    startActivityForResult(intent, LOCATION_ID);
                    emptyLocation.setVisibility(View.GONE);
                }else {
                    final Snackbar snackbar = Snackbar.make(addLocation, "Check your internet connection and try again.", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_location_recy);
        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 8, true);
        recyclerView.addItemDecoration(decorator);

        MyLocationData locationData = new MyLocationData(this);
        locationData.getLocation(recyclerView, null, emptyLocation, errorLocation, locationLoader, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOCATION_ID) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    final Location location = data.getParcelableExtra("location");
                    final MyLocationData locationData = new MyLocationData(this);
                    final Dialog dialog = new Dialog(GEMyLocationActivity.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.location_description_dialog);
                    dialog.show();
                    Button discard = (Button) dialog.findViewById(R.id.discard_location);
                    Button save = (Button) dialog.findViewById(R.id.save_location);
                    final EditText desc = (EditText) dialog.findViewById(R.id.location_description);
                    discard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Toast.makeText(GEMyLocationActivity.this, "Location discarded", Toast.LENGTH_LONG).show();
                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(desc.getText().toString().isEmpty()) {
                                Toast.makeText(GEMyLocationActivity.this, "Empty/invalid description", Toast.LENGTH_LONG).show();
                            }else if(desc.getText().toString().trim().length() < 12) {
                                Toast.makeText(GEMyLocationActivity.this, "Short description", Toast.LENGTH_LONG).show();
                            }else {
                                String descriptionText = desc.getText().toString().trim();
                                location.setDescription(descriptionText);
                                locationData.addLocation(location);
                                dialog.dismiss();
                                Toast.makeText(GEMyLocationActivity.this, location.getAddress().concat(" has been added to locations"), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }
    }
}
