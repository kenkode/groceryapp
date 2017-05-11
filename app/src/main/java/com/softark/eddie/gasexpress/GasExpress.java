package com.softark.eddie.gasexpress;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteAccessPermException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softark.eddie.gasexpress.adapters.DistributorAdapter;
import com.softark.eddie.gasexpress.data.SizeData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;
import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class GasExpress extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView sizeRecyclerView;
    private RecyclerDecorator recyclerDecorator;
    private DistributorAdapter adapter;
    private SizeData sizeData;
    private GEPreference preference;
    private TextView userName, userPhone;
    private Map<String, String> user;
    private ImageButton accessories, bulkGas;
    private ProgressBar sizeProgressBar;
    private LinearLayout errorLayout;
    private ImageView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_express);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        errorLayout.setVisibility(View.GONE);

        sizeData = new SizeData(this);
        preference = new GEPreference(this);

        accessories = (ImageButton) findViewById(R.id.acc_and_services_more_info);
        bulkGas = (ImageButton) findViewById(R.id.bulk_gas_more_info);
        sizeProgressBar = (ProgressBar) findViewById(R.id.gas_size_progress);

        refreshView = (ImageView) findViewById(R.id.refresh_page);

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

        bulkGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GasExpress.this, GEBulkGasActivity.class));
            }
        });

        accessories.setOnClickListener(new View.OnClickListener() {
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        userName = (TextView) headerView.findViewById(R.id.ge_username);
        userPhone = (TextView) headerView.findViewById(R.id.ge_userphone);
        user = preference.getUser();
        userName.setText(user.get(GEPreference.USER_NAME));
        userPhone.setText(user.get(GEPreference.USER_PHONE));

        recyclerDecorator = new RecyclerDecorator(this, 2, 10, true);

        sizeRecyclerView = (RecyclerView) findViewById(R.id.distr_list);
        sizeRecyclerView.addItemDecoration(recyclerDecorator);
        sizeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadData();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if(preference.isUserLogged()) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setText(user.get(GEPreference.USER_NAME));
        userPhone.setText(user.get(GEPreference.USER_PHONE));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.history) {
            Intent intent = new Intent(GasExpress.this, PreviousPurchasesActivity.class);
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

    public void loadData() {
        sizeData.getSizes(errorLayout, sizeRecyclerView, sizeProgressBar);
    }

}
