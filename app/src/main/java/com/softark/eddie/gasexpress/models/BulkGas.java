package com.softark.eddie.gasexpress.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BulkGas extends RealmObject {

    @PrimaryKey
    private String id;
    private int quantity;
    private double price;
    private int metric;
    private String name;

    public int getMetric() {
        return metric;
    }

    public void setMetric(int metric) {
        this.metric = metric;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
