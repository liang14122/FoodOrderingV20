package com.example.a16004118.foodorderingv20.Object;

/**
 * Created by 16004118 on 8/11/2017.
 */

public class Menu {
    private int menuId;
    private int categoryId;
    private String foodName;
    private double price;
    private String description;
    private boolean mostSeller;

    public Menu(int menuId, int categoryId, String foodName, double price, String description, boolean mostSeller) {
        this.menuId = menuId;
        this.categoryId = categoryId;
        this.foodName = foodName;
        this.price = price;
        this.description = description;
        this.mostSeller = mostSeller;
    }

    public Menu() {
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMostSeller() {
        return mostSeller;
    }

    public void setMostSeller(boolean mostSeller) {
        this.mostSeller = mostSeller;
    }
}
