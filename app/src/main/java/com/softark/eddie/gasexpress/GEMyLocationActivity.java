package com.softark.eddie.gasexpress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softark.eddie.gasexpress.data.MyLocationData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.models.Location;

import static com.softark.eddie.gasexpress.Constants.LOCATION_ID;
public class GEMyLocationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyLocationData locationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gemy_location);

        recyclerView = (RecyclerView) findViewById(R.id.my_location_recy);
        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 8, true);
        recyclerView.addItemDecoration(decorator);

        locationData = new MyLocationData(this);
        locationData.getLocation(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_location:
                Intent intent = new Intent(GEMyLocationActivity.this, GELocation.class);
                startActivityForResult(intent, LOCATION_ID);
        }
        return true;
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
