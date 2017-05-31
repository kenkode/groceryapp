package com.softark.eddie.gasexpress.data;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softark.eddie.gasexpress.R;
import com.softark.eddie.gasexpress.Retrofit.RetrofitInterface;
import com.softark.eddie.gasexpress.Retrofit.ServiceGenerator;
import com.softark.eddie.gasexpress.activities.GEMyLocationActivity;
import com.softark.eddie.gasexpress.adapters.LocationAdapter;
import com.softark.eddie.gasexpress.helpers.Checkout;
import com.softark.eddie.gasexpress.helpers.GEPreference;
import com.softark.eddie.gasexpress.helpers.GsonHelper;
import com.softark.eddie.gasexpress.models.Location;
import com.softark.eddie.gasexpress.models.RLocation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyLocationData {

    private final Context context;
    private final GEPreference preference;
    private static LocationAdapter adapter;
    private static ArrayList<Location> locations;

    public MyLocationData(Context context) {
        this.context = context;
        preference = new GEPreference(context);
    }

    public void getLocation(final RecyclerView recyclerView,
                            final Spinner spinner,
                            final LinearLayout emptyLocation,
                            final LinearLayout errorLocation,
                            final ProgressBar loader, final Button checkout) {
        locations = new ArrayList<>();
        final List<String> list = new ArrayList<>();

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        Call<List<Location>> callLocations = retrofitInterface.getLocations(preference.getUser().get(GEPreference.USER_ID));
        callLocations.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, retrofit2.Response<List<Location>> response) {
                List<Location> locationList = response.body();
                for (Location location :
                        locationList) {
                    locations.add(location);
                    list.add(location.getAddress());
                }

                if(emptyLocation != null) {
                    if(locationList.size() <= 0) {
                        emptyLocation.setVisibility(View.VISIBLE);
                    }else {
                        emptyLocation.setVisibility(View.GONE);
                    }
                }

                if (errorLocation != null) {
                    errorLocation.setVisibility(View.GONE);
                }
                if (loader != null) {
                    loader.setVisibility(View.GONE);
                }

                if (recyclerView != null) {
                    adapter = new LocationAdapter(context, locations);
                    recyclerView.setAdapter(adapter);
                }

                if (spinner != null) {
                    spinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom, list));
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Location location = locations.get(position);
                            Checkout.setLocation(location);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (checkout != null && list.size() > 0) {
                    checkout.setEnabled(true);
                } else {
                    if (list.size() < 1) {
                        if (spinner != null) {
                            final Snackbar snackbar = Snackbar.make(spinner, "Add a delivery location", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Add", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                    Intent intent = new Intent(context, GEMyLocationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            });
                            snackbar.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(final Call<List<Location>> call, Throwable t) {
                if (errorLocation != null) {
                    errorLocation.setVisibility(View.VISIBLE);
                }
                if (emptyLocation != null) {
                    emptyLocation.setVisibility(View.GONE);
                }
                if (loader != null) {
                    loader.setVisibility(View.GONE);
                }

                final Snackbar snackbar = Snackbar.make(recyclerView, "Oops! Something went wrong", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        if (errorLocation != null) {
                            errorLocation.setVisibility(View.GONE);
                        }
                        if (loader != null) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        call.cancel();
                        getLocation(recyclerView, spinner, emptyLocation, errorLocation, loader, checkout);
                    }
                });
                snackbar.show();
            }
        });
    }

    public void addLocation(final Location location) {

        RLocation rLocation = new RLocation();
        rLocation.setId(location.getId());
        rLocation.setAddress(location.getAddress());
        rLocation.setDescription(location.getDescription());
        rLocation.setLat(location.getLat());
        rLocation.setLng(location.getLng());
        rLocation.setType(location.getType());
        Gson gson = GsonHelper.getBuilder().create();
        String loc = gson.toJson(rLocation);

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);

        Call<String> addLocation = retrofitInterface.addLocation(loc, preference.getUser().get(GEPreference.USER_ID));
        addLocation.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                location.setId(response.body());
                locations.add(location);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void disableLocation(final String id) {

        RetrofitInterface retrofitInterface = ServiceGenerator.getClient().create(RetrofitInterface.class);
        Call<String> disableLocation = retrofitInterface.disableLocation(id, preference.getUser().get(GEPreference.USER_ID));

        disableLocation.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Toast.makeText(context, response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

}
