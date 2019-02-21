package com.example.bookshop.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.PreferenceHelper;
import com.google.gson.Gson;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    public CommonHelper commonHelper;
    public PreferenceHelper preferenceHelper;

    public List<Book> cart;
    public List<Book> wishList;
    public Book book;
    public Gson gson;

    public DetailsViewModel() {
    }

    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> subTitle = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();

    public MutableLiveData<Boolean> addedToCart = new MutableLiveData<>();
    public MutableLiveData<Boolean> addedToWishList = new MutableLiveData<>();

    public void onAddToCartClick(View view) {
        addedToCart.setValue(true);
    }

    public void onWishListClick(View view) {
        addedToWishList.setValue(true);
    }
}
