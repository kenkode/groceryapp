package com.softark.eddie.gasexpress.RealmData;

import com.softark.eddie.gasexpress.helpers.Cart;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class ServiceData {

    private final ArrayList<Service> services = new ArrayList<>();

    public ArrayList<Service> getServices() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> cartItems = realm.where(CartItem.class)
                .equalTo("type", Cart.SERVICES)
                .equalTo("status", 0)
                .findAll();
        for (CartItem item: cartItems) {
            Service service = new Service();
            service.setId(item.getId());
            service.setName(item.getName());
            service.setStatus(0);
            services.add(service);
        }

        return services;
    }

}
