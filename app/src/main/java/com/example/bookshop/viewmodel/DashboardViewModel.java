package com.example.bookshop.viewmodel;

import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.RecyclerView;

import com.example.bookshop.common.Constants;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Response;
import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.ui.adapter.BookAdapter;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.PreferenceHelper;
import com.example.bookshop.utils.ResponseCallback;
import com.google.gson.Gson;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private BookRepository bookRepository;
    private ApiClient apiClient;

    public ProgressDialog mProgress;
    public CommonHelper commonHelper;
    public PreferenceHelper preferenceHelper;

    public BookAdapter bookAdapter;
    public RecyclerView bookRecyclerView;
    public List<Book> books;
    public Gson gson;

    public DashboardViewModel() {
        apiClient = new ApiClient();
        bookRepository = new BookRepository(apiClient);
    }

    public MutableLiveData<List<Book>> bookLiveDataList;
    private MutableLiveData<Response> response;

    public MutableLiveData<List<Book>> getBooks() {

        if (bookLiveDataList == null) {
            bookLiveDataList = new MutableLiveData<>();
        }
        return bookLiveDataList;
    }

    public MutableLiveData<Response> getResponse() {
        if (response == null) {
            response = new MutableLiveData<>();
        }
        return response;
    }

    public void loadBooks() {

        commonHelper.showLoader(mProgress, false, Constants.LOADING);
        bookRepository.loadBooks(new ResponseCallback() {
            @Override
            public void responseHandler(String res, int tag, int statusCode) {
                Response apiResponse = new Response(statusCode, res == null ? "" : res);
                response.setValue(apiResponse);
            }
        });
    }
}
