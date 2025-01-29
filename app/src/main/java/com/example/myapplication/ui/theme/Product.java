package com.example.myapplication.ui.theme;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("price")
    private double price;

    @SerializedName("tax")
    private double tax;

    @SerializedName("product_type")
    private String productType;

    @SerializedName("image")
    private String imageUrl;

    private boolean isUploaded;  // Tracks if the product is uploaded
    private boolean isSynced;    // Tracks if the product is synced with the local DB

    public Product(String productName, double price, double tax, String productType, String imageUrl, boolean isUploaded, boolean isSynced) {
        this.productName = productName;
        this.price = price;
        this.tax = tax;
        this.productType = productType;
        this.imageUrl = imageUrl;
        this.isUploaded = isUploaded;
        this.isSynced = isSynced;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isUploaded() { return isUploaded; }
    public void setUploaded(boolean uploaded) { isUploaded = uploaded; }

    public boolean isSynced() { return isSynced; }
    public void setSynced(boolean synced) { isSynced = synced; }
}
