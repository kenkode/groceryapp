package com.softark.eddie.gasexpress.models;

import io.realm.RealmObject;

/**
 * Created by Eddie on 5/12/2017.
 */

public class OrderPrice extends RealmObject {

    private String id;
    private double price;

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
