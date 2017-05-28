package com.softark.eddie.gasexpress.models;

public class UserAuth {

    private String status;
    private String pin;
    private User user;
    private String token;

    public UserAuth(String status, String pin, User user, String token) {
        this.status = status;
        this.pin = pin;
        this.user = user;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
