package com.example.bookshop.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.view.View;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.PreferenceHelper;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    public CommonHelper commonHelper;
    public PreferenceHelper preferenceHelper;

    public List<Book> cart;
    public List<Book> wishList;
    public Book book;

    public DetailsViewModel() {
    }


    public void onAddToCartClick(View view) {

    }

    public void onWishListClick(View view) {

    }
}
