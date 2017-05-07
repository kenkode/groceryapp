package com.softark.eddie.gasexpress.models;

/**
 * Created by Eddie on 5/3/2017.
 */

public class Accessory {

    private String id;
    private String name;
    private int quantity;
    private double price;

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity() {
        quantity++;
    }

    public void decQuantity() {
        quantity--;
        if(quantity <= 0) {
            quantity = 0;
        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
