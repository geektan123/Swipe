package com.example.myapplication.ui.theme;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ProductFragment extends Fragment {
    private ProductViewModel viewModel;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private EditText searchView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout; // Added SwipeRefreshLayout

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout); // Initialize SwipeRefreshLayout
        FloatingActionButton fabAddProduct = view.findViewById(R.id.fabAddProduct);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getProductList().observe(getViewLifecycleOwner(), products -> {
            adapter.updateList(products);
            swipeRefreshLayout.setRefreshing(false); // Stop refresh animation
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAddProduct.setOnClickListener(v -> {
            AddProductBottomSheet bottomSheet = new AddProductBottomSheet();
            bottomSheet.show(getParentFragmentManager(), "AddProduct");
        });

        // Swipe to Refresh Logic
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.fetchProducts(); // Call method to refresh product list
        });

        return view;
    }

    private void filter(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : viewModel.getProductList().getValue()) {
            if (product.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        adapter.updateList(filteredList);
    }
}
