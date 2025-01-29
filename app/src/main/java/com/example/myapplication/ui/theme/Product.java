package com.example.myapplication.ui.theme;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @SerializedName("image")
    private String imageUrl;

    // Local image path for offline storage
    private String localImagePath;

    @SerializedName("price")
    private double price;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_type")
    private String productType;

    @SerializedName("tax")
    private double tax;

    private boolean isUploaded;

    // Additional fields for offline sync
    private boolean isPendingSync;

    @TypeConverters(DateConverter.class)
    private Date createdAt;

    @TypeConverters(DateConverter.class)
    private Date lastSyncAttempt;

    private int syncAttempts;

    // Constructor
    public Product() {
        this.createdAt = new Date();
        this.isUploaded = false;
        this.isPendingSync = false;
        this.syncAttempts = 0;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public boolean isPendingSync() {
        return isPendingSync;
    }

    public void setPendingSync(boolean pendingSync) {
        isPendingSync = pendingSync;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastSyncAttempt() {
        return lastSyncAttempt;
    }

    public void setLastSyncAttempt(Date lastSyncAttempt) {
        this.lastSyncAttempt = lastSyncAttempt;
    }

    public int getSyncAttempts() {
        return syncAttempts;
    }

    public void setSyncAttempts(int syncAttempts) {
        this.syncAttempts = syncAttempts;
    }

    public void incrementSyncAttempts() {
        this.syncAttempts++;
        this.lastSyncAttempt = new Date();
    }

    // Method to check if sync should be retried based on attempts
    public boolean shouldRetrySync() {
        // Allow up to 5 sync attempts with exponential backoff
        return syncAttempts < 5;
    }

    // Method to get the effective image path (either local or remote)
    public String getEffectiveImagePath() {
        return isUploaded ? imageUrl : localImagePath;
    }
}

