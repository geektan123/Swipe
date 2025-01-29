package com.example.myapplication.ui.theme;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("https://app.getswipe.in/api/public/add")
    Call<ProductResponse> addProduct(
            @Part("product_name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("tax") RequestBody tax,
            @Part("product_type") RequestBody type,
            @Part MultipartBody.Part image
    );
}
