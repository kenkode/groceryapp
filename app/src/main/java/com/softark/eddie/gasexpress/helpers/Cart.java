package com.softark.eddie.gasexpress.helpers;

import android.util.Log;
import android.widget.Toast;

import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.OrderPrice;
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

    public static double totalPrice = 0.0;
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

    public static void addGas(Gas gas) {
        saveCartItem(gas, GASES);
    }

    private static void updatePrice() {
        Realm realm = Realm.getDefaultInstance();
        OrderPrice orderPrice = realm.where(OrderPrice.class)
                .equalTo("id", OrderKey.orderKey).findFirst();
        realm.beginTransaction();
        if(orderPrice == null) {
            orderPrice = new OrderPrice();
            orderPrice.setId(OrderKey.orderKey);
            orderPrice.setPrice(totalPrice);
            realm.copyToRealm(orderPrice);
        }else {
            orderPrice.setPrice(totalPrice);
        }
        realm.commitTransaction();
    }

    public static void addBulkGas(BulkGas gas) {
        saveCartItem(gas, BULK_GAS);
    }

    public static void addProduct(Accessory accessory) {
        saveCartItem(accessory, ACCESSORIES);
    }

    public static void addService(Service service) {
        saveCartItem(service, SERVICES);
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

    public static void clearCart() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> items = realm.where(CartItem.class).findAll();
        realm.beginTransaction();
        items.deleteAllFromRealm();
        realm.commitTransaction();
        totalPrice = 0.0;
        updatePrice();
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
                    totalPrice+=gas.getPrice();
                    cartItem.setId(gas.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(1);
                    cartItem.setPrice(gas.getPrice());
                    cartItem.setName(gas.getName());
                    cartItem.setStatus(0);
                    cartItem.setType(GASES);
                    realm.beginTransaction();
                    realm.copyToRealm(cartItem);
                    realm.commitTransaction();
                }
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
                    totalPrice+=accessory.getPrice();
                    cartItem.setId(accessory.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(1);
                    cartItem.setPrice(accessory.getPrice());
                    cartItem.setName(accessory.getName());
                    cartItem.setStatus(0);
                    cartItem.setType(ACCESSORIES);
                    realm.beginTransaction();
                    realm.copyToRealm(cartItem);
                    realm.commitTransaction();
                }
                break;
            case BULK_GAS:
                BulkGas bulkGas = (BulkGas) object;
                if(realm.where(CartItem.class).equalTo("id", bulkGas.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", BULK_GAS)
                        .findFirst() == null) {
                    totalPrice+=bulkGas.getPrice();
                    cartItem.setId(bulkGas.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(1);
                    cartItem.setStatus(0);
                    cartItem.setPrice(bulkGas.getPrice());
                    cartItem.setName(bulkGas.getName());
                    cartItem.setType(BULK_GAS);
                    realm.beginTransaction();
                    realm.copyToRealm(cartItem);
                    realm.commitTransaction();
                }
                break;
        }
        updatePrice();
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
                    realm.beginTransaction();
                    g.deleteFromRealm();
                    realm.commitTransaction();
                    double itemPrice = gas.getPrice() * gas.getQuantity();
                    totalPrice-=itemPrice;
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
                    a.deleteFromRealm();
                    realm.commitTransaction();
                    double itemPrice = accessory.getPrice() * accessory.getQuantity();
                    totalPrice-=itemPrice;
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
                    bG.deleteFromRealm();
                    realm.commitTransaction();
                    double itemPrice = bulkGas.getPrice() * bulkGas.getQuantity();
                    totalPrice-=itemPrice;
                }
                break;
        }
        updatePrice();
    }

    public static void updateCartItem(Object object,int type, int qty) {
        Realm realm = Realm.getDefaultInstance();
        switch (type) {
            case GASES:
                Gas gas = (Gas) object;
                if(realm.where(CartItem.class)
                        .equalTo("id", gas.getId())
                        .equalTo("type", GASES)
                        .equalTo("orderId", OrderKey.orderKey).findFirst() != null) {
                    CartItem g = realm.where(CartItem.class).equalTo("id", gas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    realm.beginTransaction();
                    g.setQuantity(qty);
                    realm.commitTransaction();
                    double itemPrice;
                    if(gas.getQuantity() > g.getQuantity()) {
                        itemPrice = gas.getPrice() * (gas.getQuantity() - g.getQuantity());
                        totalPrice -= itemPrice;
                    }else {
                        itemPrice = gas.getPrice() * (g.getQuantity() - gas.getQuantity());
                        totalPrice += itemPrice;
                    }

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
                    a.setQuantity(qty);
                    realm.commitTransaction();
                    double itemPrice;
                    if(accessory.getQuantity() > a.getQuantity()) {
                        itemPrice = accessory.getPrice() * (accessory.getQuantity() - a.getQuantity());
                        totalPrice -= itemPrice;
                    }else {
                        itemPrice = accessory.getPrice() * (a.getQuantity() - accessory.getQuantity());
                        totalPrice += itemPrice;
                    }
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
                    bG.setQuantity(qty);
                    realm.commitTransaction();
                    double itemPrice;
                    if(bulkGas.getQuantity() > bG.getQuantity()) {
                        itemPrice = bulkGas.getPrice() * (bulkGas.getQuantity() - bG.getQuantity());
                        totalPrice -= itemPrice;
                    }else {
                        itemPrice = bulkGas.getPrice() * (bG.getQuantity() - bulkGas.getQuantity());
                        totalPrice += itemPrice;
                    }
                }
                break;
        }
        updatePrice();
    }



}
