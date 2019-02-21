package com.example.bookshop.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.RecyclerView;

import com.example.bookshop.model.Book;
import com.example.bookshop.ui.adapter.BookAdapter;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.PreferenceHelper;
import com.google.gson.Gson;

import java.util.List;

public class CartWishlistViewModel extends ViewModel {

    public CommonHelper commonHelper;
    public PreferenceHelper preferenceHelper;

    public BookAdapter bookAdapter;
    public RecyclerView bookRecyclerView;
    public List<Book> books;
    public Gson gson;

    public CartWishlistViewModel() {

    }
}
