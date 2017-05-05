package com.softark.eddie.gasexpress.helpers;

import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 5/5/2017.
 */

public class Cart {

    public static final int GASES = 0;
    public static final int ACCESSORIES = 1;
    public static final int SERVICES = 2;
    private static ArrayList<Gas> gases;
    private static ArrayList<Accessory> products;
    private static ArrayList<Service> services;

    private Cart() {
        gases = new ArrayList<>();
        products = new ArrayList<>();
        services = new ArrayList<>();
    }

    private static class Helper {
        private static final Cart CART = new Cart();
    }

    public static Cart getInstance() {
        return Helper.CART;
    }

    public static void addGas(Gas gas) {
        gases.add(gas);
    }

    public static void addProduct(Accessory accessory) {
        products.add(accessory);
    }

    public static void addService(Service service) {
        services.add(service);
    }

    public static void removeGas(int position) {
        gases.remove(position);
    }

    public static void removeProduct(int position) {
        products.remove(position);
    }

    public static void removeService(int position) {
        services.remove(position);
    }

    public List<?> getCart() {
        List<ArrayList<?>> cart = new ArrayList<>();
        cart.add(GASES, gases);
        cart.add(ACCESSORIES, products);
        cart.add(SERVICES, services);
        return cart;
    }

    public static void clearCart() {
        if(!gases.isEmpty()) {
            gases.clear();
        }
        if(!products.isEmpty()) {
            products.clear();
        }
        if(!services.isEmpty()) {
            services.clear();
        }
    }
}
