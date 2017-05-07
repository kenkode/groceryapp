package com.softark.eddie.gasexpress.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eddie on 4/30/2017.
 */

public class Gas implements Parcelable {
    private String id;
    private String name;
    private int size;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.price);
        dest.writeString(this.name);
        dest.writeInt(this.size);
        dest.writeString(this.id);
    }

    public Gas() {
    }

    protected Gas(Parcel in) {
        this.price = in.readDouble();
        this.name = in.readString();
        this.size = in.readInt();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<Gas> CREATOR = new Parcelable.Creator<Gas>() {
        @Override
        public Gas createFromParcel(Parcel source) {
            return new Gas(source);
        }

        @Override
        public Gas[] newArray(int size) {
            return new Gas[size];
        }
    };
}
