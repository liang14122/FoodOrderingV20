package com.example.a16004118.foodorderingv20.Object;

public class Order {

    private int  orderId;
    private int userId;
    private String orderTime;

    public Order( int orderId, int userId, String orderTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderTime = orderTime;
    }

    public Order() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
