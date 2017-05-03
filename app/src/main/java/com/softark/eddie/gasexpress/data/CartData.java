package com.softark.eddie.gasexpress.data;

import com.softark.eddie.gasexpress.models.CartItem;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Eddie on 5/3/2017.
 */

public class CartData {

    public ArrayList<CartItem> getCart() {
        ArrayList<CartItem> cart = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CartItem item = new CartItem();
            item.setName("Cylinder");
            item.setPrice(456.9);
            item.setId(UUID.randomUUID().toString());
            cart.add(item);
        }
        return cart;
    }

}
