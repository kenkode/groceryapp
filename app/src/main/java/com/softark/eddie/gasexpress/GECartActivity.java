package com.softark.eddie.gasexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.softark.eddie.gasexpress.adapters.CartAdapter;
import com.softark.eddie.gasexpress.data.CartData;
import com.softark.eddie.gasexpress.decorators.RecyclerDecorator;

public class GECartActivity extends AppCompatActivity {

    private RecyclerView shoppingList;
    private CartData cartData;
    private CartAdapter adapter;
    private Button clearCart, checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartData = new CartData();
        adapter = new CartAdapter(this, cartData.getCart());

        RecyclerDecorator decorator = new RecyclerDecorator(this, 1, 4, true);

        clearCart = (Button) findViewById(R.id.clear_cart);
        checkout = (Button) findViewById(R.id.check_out);
        shoppingList = (RecyclerView) findViewById(R.id.shopping_list);
        shoppingList.addItemDecoration(decorator);
        shoppingList.setAdapter(adapter);

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
