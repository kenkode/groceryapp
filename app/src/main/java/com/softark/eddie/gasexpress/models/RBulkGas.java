package com.softark.eddie.gasexpress.models;

public class RBulkGas {

    private int size;
    private int metric;
    private String id;
    private double price;

    public RBulkGas(int size, int metric, String id, double price) {
        this.size = size;
        this.metric = metric;
        this.id = id;
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
}
