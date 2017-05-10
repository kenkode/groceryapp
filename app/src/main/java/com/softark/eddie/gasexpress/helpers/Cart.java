package com.softark.eddie.gasexpress.helpers;

import android.widget.Toast;

import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Eddie on 5/5/2017.
 */

public class Cart {

    private static double totalPrice = 0.0;
    private static boolean itemExists = false;
    public static final int GASES = 0;
    public static final int ACCESSORIES = 1;
    public static final int SERVICES = 2;
    public static final int BULK_GAS = 3;
    private static ArrayList<Gas> gases;
    private static ArrayList<BulkGas> bulkGases;
    private static ArrayList<Accessory> products;
    private static ArrayList<Service> services;

    private Cart() {
        gases = new ArrayList<>();
        products = new ArrayList<>();
        services = new ArrayList<>();
        bulkGases = new ArrayList<>();
        itemExists = false;
    }

    public String generateOrderId() {
        return UUID.randomUUID().toString();
    }

    private static class Helper {
        private static final Cart CART = new Cart();
    }

    public static Cart getInstance() {
        return Helper.CART;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }

    public static Gas addGas(Gas gas) {
        saveCartItem(gas, GASES);
        totalPrice+=gas.getPrice();
        for (Gas g : gases) {
            if(g.getId().equals(gas.getId())) {
                if(g.getSize() == gas.getSize()) {
                    return g;
                }
            }
        }
        gases.add(gas);
        return gas;
    }

    public static BulkGas addBulkGas(BulkGas gas) {
        saveCartItem(gas, BULK_GAS);
        totalPrice+=gas.getPrice();
        for (BulkGas g : bulkGases) {
            if(g.getId().equals(gas.getId())) {
                return g;
            }
        }
        bulkGases.add(gas);
        return gas;
    }

    public static Accessory addProduct(Accessory accessory) {
        saveCartItem(accessory, ACCESSORIES);
        totalPrice+=accessory.getPrice();
        for (Accessory a: products) {
            if(a.getId().equals(accessory.getId())) {
                return a;
            }
        }
        products.add(accessory);
        return accessory;
    }

    public static void addService(Service service) {
        saveCartItem(service, SERVICES);
        for (Service s : services) {
            if (s.getId().equals(service.getId())) {
                return;
            }
        }
        services.add(service);
    }

    public static void removeGas(Gas gas) {
        removeCartItem(gas, Cart.GASES);
    }

    public static void removeBulkGas(BulkGas bulkGas) {
        removeCartItem(bulkGas, Cart.BULK_GAS);
    }

    public static void removeProduct(Accessory accessory) {
        removeCartItem(accessory, Cart.ACCESSORIES);
    }

    public static void removeService(Service service) {
        removeCartItem(service, Cart.SERVICES);
    }

    public List<?> getCart() {
        List<ArrayList<?>> cart = new ArrayList<>();
        cart.add(GASES, gases);
        cart.add(ACCESSORIES, products);
        cart.add(SERVICES, services);
        cart.add(BULK_GAS, bulkGases);
        return cart;
    }

    public static void cartSubmit() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<CartItem> items = realm.where(CartItem.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (CartItem cartItem :
                        items) {
                    cartItem.setStatus(1);
                }
            }
        });
        OrderKey.orderKey = UUID.randomUUID().toString();
    }

    public static void clearCart() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<CartItem> items = realm.where(CartItem.class).equalTo("orderId", OrderKey.orderKey).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                items.deleteAllFromRealm();
            }
        });
    }

    public boolean isEmpty() {
        return (gases.isEmpty() && products.isEmpty() && services.isEmpty() && bulkGases.isEmpty());
    }

    public boolean isGasEmpty() {
        return gases.isEmpty();
    }

    public boolean isBulkEmpty() {
        return products.isEmpty();
    }

    public boolean isAccessoryEmpty() {
        return products.isEmpty();
    }

    private static void saveCartItem(Object object, int type) {
        Realm realm = Realm.getDefaultInstance();
        CartItem cartItem = new CartItem();
        switch (type) {
            case GASES:
                Gas gas = (Gas) object;
                if(realm.where(CartItem.class)
                        .equalTo("id", gas.getId())
                        .equalTo("type", GASES)
                        .equalTo("orderId", OrderKey.orderKey).findFirst() == null) {
                    cartItem.setId(gas.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(1);
                    cartItem.setName(gas.getName());
                    cartItem.setStatus(0);
                    cartItem.setType(GASES);
                    realm.beginTransaction();
                    realm.copyToRealm(cartItem);
                }else {
                    realm.beginTransaction();
                    CartItem g = realm.where(CartItem.class).equalTo("id", gas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    int size = g.getQuantity();
                    size++;
                    g.setQuantity(size);
                }
                realm.commitTransaction();
                break;
            case SERVICES:
                Service service = (Service) object;
                if(realm.where(CartItem.class).equalTo("id", service.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", SERVICES)
                        .findFirst() == null) {
                    cartItem.setId(service.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(1);
                    cartItem.setStatus(0);
                    cartItem.setName(service.getName());
                    cartItem.setType(SERVICES);
                    realm.beginTransaction();
                    realm.copyToRealm(cartItem);
                    realm.commitTransaction();
                }
                break;
            case ACCESSORIES:
                Accessory accessory = (Accessory) object;
                if(realm.where(CartItem.class).equalTo("id", accessory.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", ACCESSORIES)
                        .findFirst() == null) {
                    cartItem.setId(accessory.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(1);
                    cartItem.setName(accessory.getName());
                    cartItem.setStatus(0);
                    cartItem.setType(ACCESSORIES);
                    realm.beginTransaction();
                    realm.copyToRealm(cartItem);
                }else {
                    realm.beginTransaction();
                    CartItem a = realm.where(CartItem.class).equalTo("id", accessory.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    int size = a.getQuantity();
                    size++;
                    a.setQuantity(size);
                }
                realm.commitTransaction();
                break;
            case BULK_GAS:
                BulkGas bulkGas = (BulkGas) object;
                if(realm.where(CartItem.class).equalTo("id", bulkGas.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", BULK_GAS)
                        .findFirst() == null) {
                    cartItem.setId(bulkGas.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(1);
                    cartItem.setStatus(0);
                    cartItem.setName(bulkGas.getName());
                    cartItem.setType(BULK_GAS);
                    realm.beginTransaction();
                    realm.copyToRealm(cartItem);
                }else {
                    realm.beginTransaction();
                    CartItem bG = realm.where(CartItem.class).equalTo("id", bulkGas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    int size = bG.getQuantity();
                    size++;
                    bG.setQuantity(size);
                }
                realm.commitTransaction();
                break;
        }
        realm.close();
    }

    private static void removeCartItem(Object object, int type) {
        Realm realm = Realm.getDefaultInstance();
        switch (type) {
            case GASES:
                Gas gas = (Gas) object;
                if(realm.where(CartItem.class)
                        .equalTo("id", gas.getId())
                        .equalTo("type", GASES)
                        .equalTo("orderId", OrderKey.orderKey).findFirst() != null) {
                    CartItem g = realm.where(CartItem.class).equalTo("id", gas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    int size = g.getQuantity();
                    realm.beginTransaction();
                    if(gas.getQuantity() > 0) {
                        size--;
                        g.setQuantity(size);
                    }else {
                        g.deleteFromRealm();
                    }
                    realm.commitTransaction();
                }
                break;
            case SERVICES:
                Service service = (Service) object;
                if(realm.where(CartItem.class).equalTo("id", service.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", SERVICES)
                        .findFirst() != null) {
                    CartItem s = realm.where(CartItem.class).equalTo("id", service.getId())
                            .equalTo("orderId", OrderKey.orderKey)
                            .equalTo("type", SERVICES)
                            .findFirst();
                    realm.beginTransaction();
                    s.deleteFromRealm();
                    realm.commitTransaction();
                }
                break;
            case ACCESSORIES:
                Accessory accessory = (Accessory) object;
                if(realm.where(CartItem.class).equalTo("id", accessory.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", ACCESSORIES)
                        .findFirst() != null) {
                    realm.beginTransaction();
                    CartItem a = realm.where(CartItem.class).equalTo("id", accessory.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    int size = a.getQuantity();
                    if(a.getQuantity() > 0) {
                        size--;
                        a.setQuantity(size);
                    }else {
                        a.deleteFromRealm();
                    }
                    realm.commitTransaction();
                }
                break;
            case BULK_GAS:
                BulkGas bulkGas = (BulkGas) object;
                if(realm.where(CartItem.class).equalTo("id", bulkGas.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", BULK_GAS)
                        .findFirst() != null) {
                    realm.beginTransaction();
                    CartItem bG = realm.where(CartItem.class).equalTo("id", bulkGas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    int size = bG.getQuantity();
                    if(bG.getQuantity() > 0) {
                        size--;
                        bG.setQuantity(size);
                    }else {
                        bG.deleteFromRealm();
                    }
                    realm.commitTransaction();
                }
                break;
        }
    }
}
