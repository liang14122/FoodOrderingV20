package com.example.a16004118.foodorderingv20.Object;


public class Menu {
    private int menuId;
    private int categoryId;
    private String foodName;
    private double price;
    private String description;
    private boolean mostSeller;
    private String imageURL;

    public Menu(int menuId, int categoryId, String foodName, double price, String description, boolean mostSeller, String imageURL) {
        this.menuId = menuId;
        this.categoryId = categoryId;
        this.foodName = foodName;
        this.price = price;
        this.description = description;
        this.mostSeller = mostSeller;
        this.imageURL = imageURL;
    }



    public Menu(int i, int i1, String aa, double v, String bb, String cc, boolean b) {
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
