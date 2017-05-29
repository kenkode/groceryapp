package com.softark.eddie.gasexpress.RealmData;

import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.BulkCart;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class BulkData {

    private final ArrayList<BulkGas> bulkGases = new ArrayList<>();

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
            bulkGas.setName(item.getName());
            bulkGas.setPrice(item.getPrice());
            bulkGases.add(bulkGas);
        }

        return bulkGases;
    }

}

