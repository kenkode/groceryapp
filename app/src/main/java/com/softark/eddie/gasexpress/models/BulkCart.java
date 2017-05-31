package com.softark.eddie.gasexpress.models;

import io.realm.RealmObject;

public class BulkCart extends RealmObject {

    private String id;
    private int size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
