package com.softark.eddie.gasexpress.helpers;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.softark.eddie.gasexpress.data.OrderData;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class Checkout {

    private static Location location;
    private static Map<String, String> user;
    private final OrderData orderData;

    public Checkout (Context context) {
        orderData = new OrderData(context);
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        Checkout.location = location;
    }

    public static Map<String, String> getUser() {
        return user;
    }

    public void processOrder(String paymentMethods, ProgressDialog progressDialog) {
        Gson gson = GsonHelper.getBuilder().create();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> cartItems = realm.where(CartItem.class).equalTo("status", 0).findAll();

        List<CartItem> cartItemList = new ArrayList<>();

        for (CartItem item : cartItems) {
            CartItem cartItem = new CartItem();
            cartItem.setId(item.getId());
            cartItem.setName(item.getName());
            cartItem.setStatus(item.getStatus());
            cartItem.setType(item.getType());
            cartItem.setOrderId(item.getOrderId());
            cartItem.setQuantity(item.getQuantity());
            cartItem.setPrice(item.getPrice());
            cartItemList.add(cartItem);
        }

        String processedItems = gson.toJson(cartItemList);
        orderData.placeOrder(paymentMethods, processedItems, progressDialog);
    }

}
