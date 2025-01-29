package com.example.myapplication.ui.theme;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApi {
    @GET("https://app.getswipe.in/api/public/get")
    Call<List<Product>> getProducts();
}
