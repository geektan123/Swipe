package com.example.myapplication.ui.theme;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductRepository {
    private ProductApi productApi;

    public ProductRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.getswipe.in/api/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        productApi = retrofit.create(ProductApi.class);
    }

    public Call<List<Product>> fetchProducts() {
        return productApi.getProducts();
    }
}
