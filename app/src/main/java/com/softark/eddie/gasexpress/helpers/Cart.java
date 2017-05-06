package com.softark.eddie.gasexpress.helpers;

import com.softark.eddie.gasexpress.models.Accessory;
import com.softark.eddie.gasexpress.models.BulkGas;
import com.softark.eddie.gasexpress.models.Gas;
import com.softark.eddie.gasexpress.models.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 5/5/2017.
 */

public class Cart {

    private static double totalPrice = 0.0;

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
        if(!gases.contains(gas)) {
            gases.add(gas);
            totalPrice+=gas.getPrice();
        }
    }

    public static void addBulkGas(BulkGas gas) {
        if(!bulkGases.contains(gas)) {
            totalPrice+=gas.getPrice();
            bulkGases.add(gas);
        }
    }

    public static void addProduct(Accessory accessory) {
        if(!products.contains(accessory)) {
            totalPrice+=accessory.getPrice();
            products.add(accessory);
        }
    }

    public static void addService(Service service) {
        services.add(service);
    }

    public static void removeGas(int position) {
        totalPrice-=(gases.get(position).getPrice());
        gases.remove(position);
    }

    public static void removeBulkGas(int position) {
        totalPrice-=(bulkGases.get(position).getPrice());
        bulkGases.remove(position);
    }

    public static void removeProduct(int position) {
        totalPrice-=(gases.get(position).getPrice());
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
        cart.add(BULK_GAS, bulkGases);
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
        if(!bulkGases.isEmpty()) {
            bulkGases.clear();
        }
        totalPrice = 0.0;
    }

    public boolean isEmpty() {
        return (gases.isEmpty() && products.isEmpty() && services.isEmpty() && bulkGases.isEmpty());
    }

}
