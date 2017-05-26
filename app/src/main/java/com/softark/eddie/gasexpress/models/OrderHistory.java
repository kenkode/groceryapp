package com.softark.eddie.gasexpress.models;

import com.google.gson.annotations.SerializedName;

public class OrderHistory {

    @SerializedName("order_id")
    private String id;
    @SerializedName("price")
    private double price;
    @SerializedName("created_at")
    private String date;
    @SerializedName("type")
    private String orderType;
    @SerializedName("status")
    private int status;

    public OrderHistory(String id, double price, String date, String orderType, int status) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.orderType = orderType;
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
