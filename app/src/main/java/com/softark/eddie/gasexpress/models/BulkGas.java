package com.softark.eddie.gasexpress.models;

/**
 * Created by Eddie on 5/3/2017.
 */

public class BulkGas {

    private int size;
    private int metric;
    private int quantity;
    private String id;
    private double price;

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity() {
        quantity++;
    }

    public void decQuantity() {
        quantity--;
        if(quantity < 0) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMetric() {
        return metric;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

}
