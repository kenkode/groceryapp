package com.softark.eddie.gasexpress.models;

public class User {

    private String id;
    private String lname;
    private String fname;
    private String email;
    private String phone;
    private String birthday;

    public User(String id, String lname, String fname, String email, String phone, String birthday) {
        this.id = id;
        this.lname = lname;
        this.fname = fname;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
