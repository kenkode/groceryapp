package com.softark.eddie.gasexpress.RealmData;

import android.util.Log;

import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.helpers.OrderKey;
import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Eddie on 5/9/2017.
 */

public class BulkData {

    private ArrayList<BulkGas> bulkGases = new ArrayList<>();

    public ArrayList<BulkGas> getBulkGases() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> cartItems = realm.where(CartItem.class)
                .equalTo("type", Cart.BULK_GAS)
                .equalTo("status", 0)
                .findAll();
        for (CartItem item: cartItems) {
            BulkGas bulkGas = new BulkGas();
            bulkGas.setId(item.getId());
            bulkGas.setQuantity(item.getQuantity());
            bulkGas.setName("Bulk");
            bulkGas.setPrice(item.getPrice());
            bulkGas.setStatus(0);
            bulkGases.add(bulkGas);
        }

        return bulkGases;
    }

}

