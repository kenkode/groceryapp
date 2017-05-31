package com.softark.eddie.gasexpress.helpers;

import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkCart;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.CartItem;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.OrderPrice;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class Cart {

    public static double totalPrice = 0.0;
    public static final int GASES = 0;
    public static final int ACCESSORIES = 1;
    public static final int SERVICES = 2;
    public static final int BULK_GAS = 3;

    private Cart() {
        ArrayList<Gas> gases = new ArrayList<>();
        ArrayList<Accessory> products = new ArrayList<>();
        ArrayList<Service> services = new ArrayList<>();
        ArrayList<BulkGas> bulkGases = new ArrayList<>();
    }

    private static class Helper {
        private static final Cart CART = new Cart();
    }

    public static Cart getInstance() {
        return Helper.CART;
    }

    public static void addGas(Gas gas) {
        saveCartItem(gas, GASES);
    }

    private static void updatePrice() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> cartItems = realm.where(CartItem.class)
                .findAll();
        if (cartItems.isEmpty()) {
            totalPrice = 0.0;
        }
        OrderPrice orderPrice = realm.where(OrderPrice.class)
                .equalTo("id", OrderKey.orderKey).findFirst();
        realm.beginTransaction();
        if (orderPrice == null) {
            orderPrice = new OrderPrice();
            orderPrice.setId(OrderKey.orderKey);
            orderPrice.setPrice(totalPrice);
            realm.copyToRealm(orderPrice);
        } else {
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

    public static void clearCart() {
        OrderKey.orderKey = UUID.randomUUID().toString();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CartItem> items = realm.where(CartItem.class).findAll();
        RealmResults<BulkCart> bulkCarts = realm.where(BulkCart.class).findAll();
        realm.beginTransaction();
        items.deleteAllFromRealm();
        bulkCarts.deleteAllFromRealm();
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
                if (realm.where(CartItem.class)
                        .equalTo("id", gas.getId())
                        .equalTo("type", GASES)
                        .equalTo("orderId", OrderKey.orderKey).findFirst() == null) {
                    totalPrice += gas.getPrice();
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
                if (realm.where(CartItem.class).equalTo("id", service.getId())
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
                if (realm.where(CartItem.class).equalTo("id", accessory.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", ACCESSORIES)
                        .findFirst() == null) {
                    totalPrice += accessory.getPrice();
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
                if (realm.where(CartItem.class).equalTo("id", bulkGas.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", BULK_GAS)
                        .findFirst() == null) {
                    totalPrice += bulkGas.getPrice() * bulkGas.getQuantity();
                    cartItem.setId(bulkGas.getId());
                    cartItem.setOrderId(OrderKey.orderKey);
                    cartItem.setQuantity(bulkGas.getQuantity());
                    cartItem.setPrice(bulkGas.getPrice());
                    cartItem.setName(bulkGas.getName());
                    cartItem.setStatus(0);
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
                if (realm.where(CartItem.class)
                        .equalTo("id", gas.getId())
                        .equalTo("type", GASES)
                        .equalTo("orderId", OrderKey.orderKey).findFirst() != null) {
                    CartItem g = realm.where(CartItem.class).equalTo("id", gas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    realm.beginTransaction();
                    g.deleteFromRealm();
                    realm.commitTransaction();
                    double itemPrice = gas.getPrice() * gas.getQuantity();
                    totalPrice -= itemPrice;
                }
                break;
            case SERVICES:
                Service service = (Service) object;
                if (realm.where(CartItem.class).equalTo("id", service.getId())
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
                if (realm.where(CartItem.class).equalTo("id", accessory.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", ACCESSORIES)
                        .findFirst() != null) {
                    realm.beginTransaction();
                    CartItem a = realm.where(CartItem.class).equalTo("id", accessory.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    a.deleteFromRealm();
                    realm.commitTransaction();
                    double itemPrice = accessory.getPrice() * accessory.getQuantity();
                    totalPrice -= itemPrice;
                }
                break;
            case BULK_GAS:
                BulkGas bulkGas = (BulkGas) object;
                if (realm.where(CartItem.class).equalTo("id", bulkGas.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", BULK_GAS)
                        .findFirst() != null) {
                    realm.beginTransaction();
                    CartItem a = realm.where(CartItem.class).equalTo("id", bulkGas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    a.deleteFromRealm();
                    realm.commitTransaction();
                    double itemPrice = bulkGas.getPrice() * bulkGas.getQuantity();
                    totalPrice -= itemPrice;
                }
                break;
        }
        updatePrice();
    }

    public static void updateCartItem(Object object, int type, int qty) {
        Realm realm = Realm.getDefaultInstance();
        switch (type) {
            case GASES:
                Gas gas = (Gas) object;
                if (realm.where(CartItem.class)
                        .equalTo("id", gas.getId())
                        .equalTo("type", GASES)
                        .equalTo("orderId", OrderKey.orderKey).findFirst() != null) {
                    CartItem g = realm.where(CartItem.class).equalTo("id", gas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    realm.beginTransaction();
                    g.setQuantity(qty);
                    realm.commitTransaction();
                    double itemPrice;
                    if (gas.getQuantity() > g.getQuantity()) {
                        itemPrice = gas.getPrice() * (gas.getQuantity() - g.getQuantity());
                        totalPrice -= itemPrice;
                    } else {
                        itemPrice = gas.getPrice() * (g.getQuantity() - gas.getQuantity());
                        totalPrice += itemPrice;
                    }

                }
                break;
            case ACCESSORIES:
                Accessory accessory = (Accessory) object;
                if (realm.where(CartItem.class).equalTo("id", accessory.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", ACCESSORIES)
                        .findFirst() != null) {
                    realm.beginTransaction();
                    CartItem a = realm.where(CartItem.class).equalTo("id", accessory.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    a.setQuantity(qty);
                    realm.commitTransaction();
                    double itemPrice;
                    if (accessory.getQuantity() > a.getQuantity()) {
                        itemPrice = accessory.getPrice() * (accessory.getQuantity() - a.getQuantity());
                        totalPrice -= itemPrice;
                    } else {
                        itemPrice = accessory.getPrice() * (a.getQuantity() - accessory.getQuantity());
                        totalPrice += itemPrice;
                    }
                }
                break;
            case BULK_GAS:
                BulkGas bulkGas = (BulkGas) object;
                if (realm.where(CartItem.class).equalTo("id", bulkGas.getId())
                        .equalTo("orderId", OrderKey.orderKey)
                        .equalTo("type", BULK_GAS)
                        .findFirst() != null) {
                    realm.beginTransaction();
                    CartItem a = realm.where(CartItem.class).equalTo("id", bulkGas.getId()).equalTo("orderId", OrderKey.orderKey).findFirst();
                    a.setQuantity(qty);
                    realm.commitTransaction();
                    double itemPrice;
                    if (bulkGas.getQuantity() > a.getQuantity()) {
                        itemPrice = bulkGas.getPrice() * (bulkGas.getQuantity() - a.getQuantity());
                        totalPrice -= itemPrice;
                    } else {
                        itemPrice = bulkGas.getPrice() * (a.getQuantity() - bulkGas.getQuantity());
                        totalPrice += itemPrice;
                    }
                }
                break;
        }
        updatePrice();
    }

//    public static void updateBulkPrice(int quantity, double ttlPrice, double oldPrice, double newPrice) {
//        double price;
//        Realm realm = Realm.getDefaultInstance();
//        if (newPrice > oldPrice) {
//            price = newPrice - oldPrice;
//            totalPrice += price;
//        } else {
//            price = oldPrice - newPrice;
//            totalPrice -= price;
//        }
//        CartItem realmGas = realm.where(CartItem.class)
//                .equalTo("id", OrderKey.orderKey)
//                .equalTo("type", Cart.BULK_GAS)
//                .findFirst();
//
//        realm.beginTransaction();
//        if (realmGas != null) {
//            realmGas.setQuantity(quantity);
//            realmGas.setPrice(ttlPrice);
//        } else {
//            CartItem bulkGas = new CartItem();
//            bulkGas.setId(OrderKey.orderKey);
//            bulkGas.setName("Bulk Gas");
//            bulkGas.setStatus(0);
//            bulkGas.setOrderId(OrderKey.orderKey);
//            bulkGas.setQuantity(quantity);
//            bulkGas.setType(Cart.BULK_GAS);
//            bulkGas.setPrice(ttlPrice);
//            realm.copyToRealm(bulkGas);
//        }
//        realm.commitTransaction();
//        updatePrice();
//    }
}
