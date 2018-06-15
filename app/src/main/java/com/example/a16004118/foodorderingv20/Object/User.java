package com.example.a16004118.foodorderingv20.Object;

public class User {

    private int userId;
    private int fbId;
//    private String role;

    public User(int userId, int fbId) {
        this.userId = userId;
        this.fbId = fbId;
    }

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFbId() {
        return fbId;
    }

    public void setFbId(int fbId) {
        this.fbId = fbId;
    }
}
