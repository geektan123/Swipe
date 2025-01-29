package com.example.myapplication.ui.theme;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    long insertProduct(Product product);

    @Query("SELECT * FROM products WHERE isUploaded = 0")
    List<Product> getUnsyncedProducts();

    @Update
    void updateProduct(Product product);

    // Additional helpful queries
    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    List<Product> getAllProducts();

    @Query("SELECT COUNT(*) FROM products WHERE isUploaded = 0")
    int getUnsyncedProductCount();
}