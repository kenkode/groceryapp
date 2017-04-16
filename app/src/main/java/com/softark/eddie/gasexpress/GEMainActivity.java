package com.softark.eddie.gasexpress;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.softark.eddie.gasexpress.adapters.DistributorAdapter;
import com.softark.eddie.gasexpress.data.Distributors;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class GEMainActivity extends AppCompatActivity {

    private RecyclerView distributorRecyclerView;
    private DistributorAdapter adapter;
    private Distributors distributors;
    private RecyclerDecorator recyclerDecorator;
    private ImageButton menuButton;
    private ImageButton menuOrderButton;
    private ImageButton menuPriceButton;
    private ImageButton menuSummaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gemain);
        menuButton = (ImageButton) findViewById(R.id.ge_center_menu);
        menuOrderButton = (ImageButton) findViewById(R.id.ge_center_menu_order);
        menuPriceButton = (ImageButton) findViewById(R.id.ge_center_menu_prices);
        menuSummaryButton = (ImageButton) findViewById(R.id.ge_center_menu_summary);

        menuOrderButton.setVisibility(View.GONE);
        menuPriceButton.setVisibility(View.GONE);
        menuSummaryButton.setVisibility(View.GONE);
        menuOrderButton.animate().alpha(0);
        menuPriceButton.animate().alpha(0);
        menuSummaryButton.animate().alpha(0);

        menuOrderButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Toast.makeText(GEMainActivity.this, "Fouces", Toast.LENGTH_LONG).show();
                }
            }
        });

        menuOrderButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(GEMainActivity.this, "Touched", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        menuButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    showMenuButtons();
                }else if(event.getActionMasked() == MotionEvent.ACTION_UP) {
                    hideMenuButtons();
                }
                return false;
            }
        });

        recyclerDecorator = new RecyclerDecorator(this, 2, 10, true);

        distributorRecyclerView = (RecyclerView) findViewById(R.id.distr_list);
        distributorRecyclerView.addItemDecoration(recyclerDecorator);
        distributorRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        distributors = new Distributors();

        adapter = new DistributorAdapter(distributors.getDistributors(), this);

        distributorRecyclerView.setAdapter(adapter);

    }

    public void showMenuButtons() {
        menuOrderButton.setVisibility(View.VISIBLE);
        menuPriceButton.setVisibility(View.VISIBLE);
        menuSummaryButton.setVisibility(View.VISIBLE);
        menuButton.animate()
                .alpha(0);
        menuOrderButton.animate()
                .alpha(1)
                .setDuration(1000);
        menuPriceButton.animate()
                .alpha(1)
                .setDuration(1000);
        menuSummaryButton.animate()
                .alpha(1)
                .setDuration(1000);
    }

    public void hideMenuButtons() {
        menuButton.animate()
                .alpha(1)
                .setDuration(3000);
        menuOrderButton.animate()
                .alpha(0)
                .setDuration(3000);
        menuPriceButton.animate()
                .alpha(0)
                .setDuration(3000);
        menuSummaryButton.animate()
                .alpha(0)
                .setDuration(3000);
        menuOrderButton.setVisibility(View.GONE);
        menuPriceButton.setVisibility(View.GONE);
        menuSummaryButton.setVisibility(View.GONE);
    }

}
