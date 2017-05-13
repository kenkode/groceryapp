package com.softark.eddie.gasexpress.models;

import io.realm.RealmObject;

public class BulkCart extends RealmObject {

    private String id;
    private int metric, size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMetric() {
        return metric;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
