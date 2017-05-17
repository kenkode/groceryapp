package com.softark.eddie.gasexpress.RealmData;

import android.content.Context;

import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Gas;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class GasData {

    public GasData(Context context) {
        Context context1 = context;
    }

    public ArrayList<Gas> getGases() {
        ArrayList<Gas> gases = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> items = realm.where(CartItem.class)
                .equalTo("type", Cart.GASES)
                .equalTo("status", 0)
                .findAll();
        for (CartItem item :
                items) {
            Gas gas = new Gas();
            gas.setId(item.getId());
            gas.setName(item.getName());
            gas.setQuantity(item.getQuantity());
            gas.setSize(4);
            gas.setPrice(item.getPrice());
            gases.add(gas);
        }
        return gases;
    }

}
