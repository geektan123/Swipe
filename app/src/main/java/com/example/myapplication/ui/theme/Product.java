package com.example.myapplication.ui.theme;

import com.google.gson.annotations.SerializedName;
public class Product {
    @SerializedName("image")
    private String imageUrl;

    @SerializedName("price")
    private double price;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_type")
    private String productType;

    @SerializedName("tax")
    private double tax;

    private boolean isUploaded;

    // Getters & Setters
    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }
}
