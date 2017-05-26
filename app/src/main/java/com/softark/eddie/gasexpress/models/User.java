package com.softark.eddie.gasexpress.models;

import com.google.gson.annotations.Expose;

public class User {

    @Expose
    private String id;
    @Expose
    private String lname;
    @Expose
    private String fname;
    @Expose
    private String email;
    @Expose
    private String phone;
    @Expose
    private String birthday;

    public User(String lname, String fname, String email, String phone, String birthday) {
        this.lname = lname;
        this.fname = fname;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
