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
        RealmResults<BulkCart> bulkCart = realm.where(BulkCart.class)
                .findAll();
        for (CartItem item: cartItems) {
            BulkGas bulkGas = new BulkGas();
            bulkGas.setId(item.getId());
            bulkGas.setQuantity(item.getQuantity());
            for (int i = 0; i < bulkCart.size(); i++) {
                if(item.getId().equals(bulkCart.get(i).getId())) {
                    BulkCart bC = bulkCart.get(i);
                    bulkGas.setMetric(bC.getMetric());
                    bulkGas.setSize(bC.getSize());
                }
            }
            bulkGas.setName("Bulk");
            bulkGas.setPrice(item.getPrice());
            bulkGas.setStatus(0);
            bulkGases.add(bulkGas);
        }

        return bulkGases;
    }

}

