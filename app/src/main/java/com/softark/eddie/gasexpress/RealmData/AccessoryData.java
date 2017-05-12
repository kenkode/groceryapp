package com.softark.eddie.gasexpress.RealmData;

import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.OrderKey;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.CartItem;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eddie on 5/9/2017.
 */

public class AccessoryData {

    private ArrayList<Accessory> accessories = new ArrayList<>();

    public ArrayList<Accessory> getAccessories() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> cartItems = realm.where(CartItem.class)
                .equalTo("type", Cart.ACCESSORIES)
                .equalTo("status", 0)
                .findAll();
        for (CartItem acc: cartItems) {
            Accessory accessory = new Accessory();
            accessory.setId(acc.getId());
            accessory.setQuantity(acc.getQuantity());
            accessory.setName(acc.getName());
            accessory.setPrice(acc.getPrice());
            accessory.setStatus(0);
            accessories.add(accessory);
        }
        return accessories;
    }

}
