package com.example.myapplication.ui.theme;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double price;
    public double tax;
    public String type;
    public String imageUri;

    public ProductEntity(String name, double price, double tax, String type, String imageUri) {
        this.name = name;
        this.price = price;
        this.tax = tax;
        this.type = type;
        this.imageUri = imageUri;
    }
}
