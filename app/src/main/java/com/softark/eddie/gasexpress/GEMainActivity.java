package com.softark.eddie.gasexpress;

import android.content.Intent;
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

        menuOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GEMainActivity.this, GEOrderActivity.class));
            }
        });

        menuOrderButton.setVisibility(View.GONE);
        menuPriceButton.setVisibility(View.GONE);
        menuSummaryButton.setVisibility(View.GONE);
        menuOrderButton.animate().alpha(0);
        menuPriceButton.animate().alpha(0);
        menuSummaryButton.animate().alpha(0);

        menuButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    showMenuButtons();
                }else if(event.getActionMasked() == MotionEvent.ACTION_UP) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideMenuButtons();
                                }
                            });
                        }
                    }).start();
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
