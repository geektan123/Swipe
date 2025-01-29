package com.example.myapplication.ui.theme;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.R;
import com.example.myapplication.ui.theme.AppDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductBottomSheet extends BottomSheetDialogFragment {
    private TextInputEditText edtProductName, edtProductPrice, edtProductTax;
    private Spinner spinnerProductType;
    private ShapeableImageView shapeableImageView;
    private ImageView plusIcon;
    private LinearLayout uploadButton, nextButton;
    private Uri selectedImageUri;
    private ProgressDialog progressDialog;
    private AppDatabase db;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        shapeableImageView.setImageURI(selectedImageUri);
                        plusIcon.setVisibility(View.GONE);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        db = AppDatabase.getInstance(requireContext());
        initializeViews(view);
        setupSpinner();
        setupListeners();
        checkStoragePermissions();

        return view;
    }

    private void initializeViews(View view) {
        edtProductName = view.findViewById(R.id.edtProductName);
        edtProductPrice = view.findViewById(R.id.edtProductPrice);
        edtProductTax = view.findViewById(R.id.edtProductTax);
        spinnerProductType = view.findViewById(R.id.spinnerProductType);

        shapeableImageView = view.findViewById(R.id.shapeable_image_view);
        plusIcon = view.findViewById(R.id.plus);

        uploadButton = view.findViewById(R.id.Nextlay);
        nextButton = view.findViewById(R.id.Next);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving product...");
    }

    private void setupSpinner() {
        String[] productTypes = {"Product", "Service"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()),
                android.R.layout.simple_spinner_dropdown_item,
                productTypes
        );
        spinnerProductType.setAdapter(adapter);
    }

    private void setupListeners() {
        uploadButton.setOnClickListener(v -> openGallery());
        nextButton.setOnClickListener(v -> validateAndSubmit());
    }

    private void validateAndSubmit() {
        String name = edtProductName.getText().toString().trim();
        String price = edtProductPrice.getText().toString().trim();
        String tax = edtProductTax.getText().toString().trim();
        String type = spinnerProductType.getSelectedItem().toString();

        if (name.isEmpty() || price.isEmpty() || tax.isEmpty() || selectedImageUri == null) {
            Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        double priceValue, taxValue;
        try {
            priceValue = Double.parseDouble(price);
            taxValue = Double.parseDouble(tax);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Price and tax must be numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isNetworkAvailable()) {
            uploadProduct(name, priceValue, taxValue, type);
        } else {
            saveProductLocally(name, priceValue, taxValue, type);
        }
    }

    private void saveProductLocally(String name, double price, double tax, String type) {
        progressDialog.show();

        new Thread(() -> {
            try {
                // Save image to internal storage
                String imagePath = saveImageToInternalStorage(selectedImageUri);
                if (imagePath == null) {
                    showError("Failed to save image!");
                    return;
                }

                // Create product object
                Product product = new Product();
                product.setProductName(name);
                product.setPrice(price);
                product.setTax(tax);
                product.setProductType(type);
                product.setLocalImagePath(imagePath);
                product.setPendingSync(false);

                // Save to database
                long id = db.productDao().insertProduct(product);

                if (id > 0) {
                    requireActivity().runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),
                                "Product saved offline. Will sync when online.",
                                Toast.LENGTH_LONG).show();
                        scheduleSync();
                        dismiss();
                    });
                } else {
                    showError("Failed to save product locally!");
                }
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        }).start();
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            // Create directory for product images
            File directory = new File(requireContext().getFilesDir(), "product_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create unique filename
            String fileName = "IMG_" + UUID.randomUUID().toString() + ".jpg";
            File imageFile = new File(directory, fileName);

            // Copy image data
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadProduct(String name, double price, double tax, String type) {
        progressDialog.show();

        String filePath = getFileFromUri(getContext(), selectedImageUri);
        if (filePath == null) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Failed to get image path!", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("files[]", file.getName(), requestFile);

        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody taxPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tax));
        RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), type);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ProductResponse> call = apiService.addProduct(namePart, pricePart, taxPart, typePart, imagePart);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Product Added Successfully!", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    saveProductLocally(name, price, tax, type);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressDialog.dismiss();
                saveProductLocally(name, price, tax, type);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void scheduleSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest syncWork = new OneTimeWorkRequest.Builder(ProductSyncWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(requireContext()).enqueue(syncWork);
    }

    private void showError(String message) {
        requireActivity().runOnUiThread(() -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private void checkStoragePermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), permissions, 101);
                return;
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private String getFileFromUri(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null) return null;

            // Create a file in cache directory with timestamp to avoid conflicts
            String timestamp = String.valueOf(System.currentTimeMillis());
            File tempFile = new File(context.getCacheDir(), "temp_" + timestamp + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            // Use larger buffer for better performance
            byte[] buffer = new byte[8192]; // 8KB buffer
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(getContext(), "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(getContext(),
                        "Storage permission is required to select images",
                        Toast.LENGTH_LONG).show();
                dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up temporary files
        cleanupTempFiles();
    }

    private void cleanupTempFiles() {
        try {
            File cacheDir = requireContext().getCacheDir();
            File[] tempFiles = cacheDir.listFiles((dir, name) -> name.startsWith("temp_"));
            if (tempFiles != null) {
                for (File file : tempFiles) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}