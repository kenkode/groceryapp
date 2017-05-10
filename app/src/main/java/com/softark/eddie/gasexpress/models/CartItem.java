package com.softark.eddie.gasexpress.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Eddie on 5/9/2017.
 */

//    status
//    0 - only local
//    1 - both local and online
//    2 - failed
//

public class CartItem extends RealmObject {

    @Expose
    private String orderId;
    @Expose
    private String name;
    @Expose
    private String id;
    @Expose
    private int quantity;
    @Expose
    private int type;
    @Expose
    private int status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
