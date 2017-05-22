package com.softark.eddie.gasexpress.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.data.SizeData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.helpers.GEPreference;

import java.util.Map;

@SuppressWarnings("deprecation")
public class GasExpress extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView sizeRecyclerView;
    private SizeData sizeData;
    private GEPreference preference;
    private TextView userName, userPhone;
    private Map<String, String> user;
    private ProgressBar sizeProgressBar;
    private LinearLayout errorLayout;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_express);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preference = new GEPreference(this);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        errorLayout.setVisibility(View.GONE);

        sizeData = new SizeData(this);

        CardView accessoriesView = (CardView) findViewById(R.id.a_s);
        CardView bulkGasView = (CardView) findViewById(R.id.bulk_card);
        CardView serviceView = (CardView) findViewById(R.id.service_card);
        sizeProgressBar = (ProgressBar) findViewById(R.id.gas_size_progress);

        ImageView refreshView = (ImageView) findViewById(R.id.refresh_page);

        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(errorLayout.getVisibility() == View.VISIBLE) {
                    errorLayout.setVisibility(View.GONE);
                }
                sizeProgressBar.setVisibility(View.VISIBLE);
                loadData();
            }
        });

        bulkGasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GasExpress.this, GEBulkGasActivity.class));
            }
        });

        accessoriesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GasExpress.this, GEAccessoryActivity.class));
            }
        });

        serviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GasExpress.this, GEAccessoryActivity.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GasExpress.this, GECartActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        userName = (TextView) headerView.findViewById(R.id.ge_username);
        userPhone = (TextView) headerView.findViewById(R.id.ge_userphone);
        user = preference.getUser();
        userName.setText(user.get(GEPreference.USER_FNAME).concat(" ").concat(user.get(GEPreference.USER_LNAME)));

        userPhone.setText(user.get(GEPreference.USER_PHONE));

        RecyclerDecorator recyclerDecorator = new RecyclerDecorator(this, 2, 10, true);

        sizeRecyclerView = (RecyclerView) findViewById(R.id.distr_list);
        sizeRecyclerView.addItemDecoration(recyclerDecorator);
        sizeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadData();

    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setText(user.get(GEPreference.USER_FNAME).concat(" ").concat(user.get(GEPreference.USER_LNAME)));
        userPhone.setText(user.get(GEPreference.USER_PHONE));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.history) {
            Intent intent = new Intent(GasExpress.this, GEHistory.class);
            startActivity(intent);
        } else if (id == R.id.my_locations) {
            Intent intent = new Intent(GasExpress.this, GEMyLocationActivity.class);
            startActivity(intent);
        } else if(id == R.id.my_cart) {
            Intent intent = new Intent(GasExpress.this, GECartActivity.class);
            startActivity(intent);
        } else if(id == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preference.unsetUser();
                    dialog.dismiss();
                    Intent intent = new Intent(GasExpress.this, GELoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            })
             .setNegativeButton("No", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
             });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadData() {
        sizeData.getSizes(errorLayout, sizeRecyclerView, sizeProgressBar);
    }

}
