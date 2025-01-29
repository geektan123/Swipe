package com.example.myapplication.ui.theme;



import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ProductSyncWorker extends Worker {
    private final Context context;

    public ProductSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getInstance(context);
        List<Product> unsyncedProducts = db.productDao().getUnsyncedProducts();

        for (Product product : unsyncedProducts) {
            try {
                // Create MultipartBody.Part for image
                File imageFile = new File(product.getLocalImagePath());
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                        "files[]",
                        imageFile.getName(),
                        requestFile
                );

                // Create request bodies for other fields
                RequestBody namePart = RequestBody.create(
                        MediaType.parse("text/plain"),
                        product.getProductName()
                );
                RequestBody pricePart = RequestBody.create(
                        MediaType.parse("text/plain"),
                        String.valueOf(product.getPrice())
                );
                RequestBody taxPart = RequestBody.create(
                        MediaType.parse("text/plain"),
                        String.valueOf(product.getTax())
                );
                RequestBody typePart = RequestBody.create(
                        MediaType.parse("text/plain"),
                        product.getProductType()
                );

                // Make synchronous API call
                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Response<ProductResponse> response = apiService.addProduct(
                        namePart,
                        pricePart,
                        taxPart,
                        typePart,
                        imagePart
                ).execute();

                if (response.isSuccessful()) {
                    product.setPendingSync(true);
                    db.productDao().updateProduct(product);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.retry();
            }
        }

        return Result.success();
    }
}