package com.example.myapplication.ui.theme;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM products WHERE isSynced = 0")
    List<Product> getUnsyncedProducts();

    @Query("UPDATE products SET isSynced = 1 WHERE id = :productId")
    void markAsSynced(int productId);
}
