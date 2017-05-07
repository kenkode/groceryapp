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
        for (Service s : services) {
            if (s.getId().equals(service.getId())) {
                return;
            }
        }
        services.add(service);
    }

    public static void removeGas(int position) {
        totalPrice-=(gases.get(position).getPrice());
        if(gases.get(position).getQuantity() <= 0) {
            gases.remove(position);
        }
    }

    public static void removeBulkGas(int position) {
        totalPrice-=(bulkGases.get(position).getPrice());
        if(bulkGases.get(position).getQuantity() <= 0) {
            bulkGases.remove(position);
        }
    }

    public static void removeProduct(int position) {
        totalPrice-=(products.get(position).getPrice());
        if(products.get(position).getQuantity() <= 0) {
            products.remove(position);
        }
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

    public boolean isGasEmpty() {
        return gases.isEmpty();
    }

    public boolean isBulkEmpty() {
        return products.isEmpty();
    }

    public boolean isAccessoryEmpty() {
        return products.isEmpty();
    }

}
