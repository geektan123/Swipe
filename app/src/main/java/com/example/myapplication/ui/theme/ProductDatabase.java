package com.example.myapplication.ui.theme;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {
    private static ProductDatabase instance;
    public abstract ProductDao productDao();

    public static synchronized ProductDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ProductDatabase.class, "product_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
