package com.softark.eddie.gasexpress.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softark.eddie.gasexpress.PreviousPurchasesActivity;
import com.softark.eddie.gasexpress.data.OrderData;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Location;
import com.softark.eddie.gasexpress.models.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eddie on 5/7/2017.
 */

public class Checkout {

    private Context context;
    private static Location location;
    private static Map<String, String> user;
    private OrderData orderData;
    private GEPreference preference;

    public Checkout (Context context) {
        this.context = context;
        preference = new GEPreference(context);
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

    public static void setUser(Map<String, String> user) {
        Checkout.user = user;
    }

    public void processOrder() {
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
            cartItemList.add(cartItem);
        }

        String processedItems = gson.toJson(cartItemList);
        Log.i("Order", processedItems);
        orderData.placeOrder(processedItems);
    }

}
