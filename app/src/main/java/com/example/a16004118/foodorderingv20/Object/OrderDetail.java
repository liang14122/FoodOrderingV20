package com.example.a16004118.foodorderingv20.Object;

public class OrderDetail {

    private int OrderDetailId;
    private int orderId;
    private int menuId;
    private int quantity;

    public OrderDetail(int orderDetailId, int orderId, int menuId, int quantity) {
        OrderDetailId = orderDetailId;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderDetail() {
    }

    public int getOrderDetailId() {
        return OrderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        OrderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
