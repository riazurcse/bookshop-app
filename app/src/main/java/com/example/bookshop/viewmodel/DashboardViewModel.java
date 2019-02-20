package com.example.bookshop.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.repository.BookRepository;

public class DashboardViewModel extends ViewModel {

    private BookRepository bookRepository;
    private ApiClient apiClient;

    public DashboardViewModel() {
        apiClient = new ApiClient();
        bookRepository = new BookRepository(apiClient);
    }
}
