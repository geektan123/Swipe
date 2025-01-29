package com.example.myapplication.ui.theme;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insertProduct(ProductEntity product);

    @Query("SELECT * FROM products")
    List<ProductEntity> getAllProducts();
}
