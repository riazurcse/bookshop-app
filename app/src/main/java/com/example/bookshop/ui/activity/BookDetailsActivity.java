package com.example.bookshop.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.bookshop.R;
import com.example.bookshop.common.Constants;
import com.example.bookshop.common.IntentKeys;
import com.example.bookshop.databinding.ActivityBookDetailsBinding;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.PreferenceHelper;
import com.example.bookshop.viewmodel.DetailsViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {

    private static final String TAG = BookDetailsActivity.class.getName();

    private DetailsViewModel detailsViewModel;
    private ActivityBookDetailsBinding bookDetailBinding;

    Toolbar toolbar;
    RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetup();
        dataReceived();
    }

    private void initSetup() {
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        bookDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_details);
        bookDetailBinding.setDetailsViewModel(detailsViewModel);
        detailsViewModel.commonHelper = new CommonHelper(this);
        detailsViewModel.preferenceHelper = new PreferenceHelper(this);
        detailsViewModel.cart = new ArrayList<>();
        detailsViewModel.wishList = new ArrayList<>();
        detailsViewModel.gson = new Gson();
        setupToolbar();
        getSavedData();
        bookDetailBinding.setLifecycleOwner(this);

        detailsViewModel.addedToCart.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        doAddToCart();
                    }
                }
            }
        });

        detailsViewModel.addedToWishList.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        doAddToWishList();
                    }
                }
            }
        });
    }

    private void getSavedData() {
        String cartJSON = "";
        String wishListJSON = "";
        cartJSON = detailsViewModel.preferenceHelper.getUserInfo(Constants.CART_DATA) != null ? detailsViewModel.preferenceHelper.getUserInfo(Constants.CART_DATA): "";
        wishListJSON = detailsViewModel.preferenceHelper.getUserInfo(Constants.WISHLIST_DATA) != null ? detailsViewModel.preferenceHelper.getUserInfo(Constants.WISHLIST_DATA): "";
        if (cartJSON.length() > 0) {
            detailsViewModel.cart = detailsViewModel.gson.fromJson(cartJSON, new TypeToken<List<Book>>(){}.getType());
        }
        if (wishListJSON.length() > 0) {
            detailsViewModel.wishList = detailsViewModel.gson.fromJson(wishListJSON, new TypeToken<List<Book>>(){}.getType());
        }
    }

    private void dataReceived() {
        Bundle bundle = getIntent().getExtras();
        String bookDetails = bundle.getString(IntentKeys.BOOK_DETAILS);
        detailsViewModel.book = detailsViewModel.gson.fromJson(bookDetails, Book.class);
        populateData();
    }

    private void populateData() {

        detailsViewModel.title.setValue(detailsViewModel.book.getTitle() != null ? detailsViewModel.book.getTitle() : "N/A");
        detailsViewModel.subTitle.setValue(detailsViewModel.book.getSubTitle() != null ? detailsViewModel.book.getSubTitle() : "N/A");
        detailsViewModel.description.setValue(detailsViewModel.book.getDescription() != null ? detailsViewModel.book.getDescription() : "N/A");

        ImageView preview = (ImageView) bookDetailBinding.bookDetailPreviewIV;
        if (detailsViewModel.book.getPreview().length() > 0 && detailsViewModel.book.getPreview() != null) {
            String previewURL = detailsViewModel.book.getPreview();
            Glide.with(this).load(previewURL).placeholder(R.drawable.ic_placeholder).into(preview);
        }
    }

    private void doAddToCart() {
        if (!detailsViewModel.cart.isEmpty()) {
            boolean itemAlreadyAdded = false;
            for (int i = 0; i < detailsViewModel.cart.size(); i++) {
                Book book = detailsViewModel.cart.get(i);
                if (book.getId() == detailsViewModel.book.getId()) {
                    itemAlreadyAdded = true;
                    break;
                }
            }
            if (!itemAlreadyAdded) {
                detailsViewModel.cart.add(detailsViewModel.book);
                detailsViewModel.commonHelper.showSnackAlert(getString(R.string.item_added_message), parentLayout);
            }
            else {
                detailsViewModel.commonHelper.showSnackAlert(getString(R.string.already_added_in_cart_message), parentLayout);
            }
        } else {
            detailsViewModel.cart.add(detailsViewModel.book);
            detailsViewModel.commonHelper.showSnackAlert(getString(R.string.item_added_message), parentLayout);
        }

        if (!detailsViewModel.wishList.isEmpty()) {
            int indexToBeRemoved = -1;
            for (int i = 0; i < detailsViewModel.wishList.size(); i++) {
                Book book = detailsViewModel.wishList.get(i);
                if (book.getId() == detailsViewModel.book.getId()) {
                    indexToBeRemoved = i;
                    break;
                }
            }

            if (indexToBeRemoved != -1) {
                detailsViewModel.wishList.remove(indexToBeRemoved);
            }
        }
    }

    private void doAddToWishList() {
        if (!detailsViewModel.wishList.isEmpty()) {
            boolean itemAlreadyAdded = false;
            for (int i = 0; i < detailsViewModel.wishList.size(); i++) {
                Book book = detailsViewModel.wishList.get(i);
                if (book.getId() == detailsViewModel.book.getId()) {
                    itemAlreadyAdded = true;
                    break;
                }
            }
            if (!itemAlreadyAdded) {
                detailsViewModel.wishList.add(detailsViewModel.book);
                detailsViewModel.commonHelper.showSnackAlert( getString(R.string.item_wishlist_message), parentLayout);
            }
            else {
                detailsViewModel.commonHelper.showSnackAlert(getString(R.string.already_added_in_wishlist_message), parentLayout);
            }
        } else {
            detailsViewModel.wishList.add(detailsViewModel.book);
            detailsViewModel.commonHelper.showSnackAlert( getString(R.string.item_wishlist_message), parentLayout);
        }

        if (!detailsViewModel.cart.isEmpty()) {
            int indexToBeRemoved = -1;
            for (int i = 0; i < detailsViewModel.cart.size(); i++) {
                Book book = detailsViewModel.cart.get(i);
                if (book.getId() == detailsViewModel.book.getId()) {
                    indexToBeRemoved = i;
                    break;
                }
            }

            if (indexToBeRemoved != -1) {
                detailsViewModel.cart.remove(indexToBeRemoved);
            }
        }
    }

    private void setupToolbar() {
        parentLayout = (RelativeLayout) bookDetailBinding.parentLayout;
        toolbar = (Toolbar) bookDetailBinding.toolBar;
        toolbar.setTitleMarginStart(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartJSON = "";
                String wishListJSON = "";
                if (!detailsViewModel.cart.isEmpty()) {
                    cartJSON = detailsViewModel.gson.toJson(detailsViewModel.cart, new TypeToken<List<Book>>(){}.getType());
                }

                if(!detailsViewModel.wishList.isEmpty()) {
                    wishListJSON = detailsViewModel.gson.toJson(detailsViewModel.wishList, new TypeToken<List<Book>>(){}.getType());
                }
                detailsViewModel.preferenceHelper.saveUserInfo(Constants.CART_DATA, cartJSON);
                detailsViewModel.preferenceHelper.saveUserInfo(Constants.WISHLIST_DATA, wishListJSON);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
