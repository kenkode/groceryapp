package com.softark.eddie.gasexpress.core;

import android.app.Application;

import com.softark.eddie.gasexpress.helpers.OrderKey;
import com.softark.eddie.gasexpress.models.CartItem;

import java.util.UUID;

import io.realm.Realm;

/**
 * Created by Eddie on 5/9/2017.
 */

public class RealmConfiguration extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        io.realm.RealmConfiguration realmConfiguration = new io.realm.RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        Realm realm = Realm.getDefaultInstance();

        CartItem cartItem = realm.where(CartItem.class)
                .equalTo("status", 0)
                .findFirst();
        if(cartItem != null) {
            OrderKey.orderKey = cartItem.getOrderId();
        }else {
            OrderKey.orderKey = UUID.randomUUID().toString();
        }

    }
}
