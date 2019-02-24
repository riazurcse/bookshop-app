package com.example.bookshop.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.bookshop.R;
import com.example.bookshop.common.Constants;
import com.example.bookshop.common.IntentKeys;
import com.example.bookshop.databinding.ActivityCartWishlistBinding;
import com.example.bookshop.model.Book;
import com.example.bookshop.ui.adapter.CartWishlistAdapter;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.ItemClickSupport;
import com.example.bookshop.utils.PreferenceHelper;
import com.example.bookshop.viewmodel.CartWishlistViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class CartWishlistActivity extends AppCompatActivity {

    private static final String TAG = CartWishlistActivity.class.getName();

    private CartWishlistViewModel cartWishlistViewModel;
    private ActivityCartWishlistBinding cartWishlistBinding;

    Toolbar toolbar;
    boolean showCart = false;
    RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetup();
        dataReceived();
        setupToolbar();
        setupRecyclerView();
    }

    private void initSetup() {
        cartWishlistViewModel = ViewModelProviders.of(this).get(CartWishlistViewModel.class);
        cartWishlistBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart_wishlist);
        cartWishlistViewModel.commonHelper = new CommonHelper(this);
        cartWishlistViewModel.preferenceHelper = new PreferenceHelper(this);
        cartWishlistViewModel.gson = new Gson();
        cartWishlistBinding.setLifecycleOwner(this);
        cartWishlistBinding.setCartWishlistViewModel(cartWishlistViewModel);
    }

    private void dataReceived() {
        Bundle bundle = getIntent().getExtras();
        showCart = bundle.getBoolean(IntentKeys.SHOW_CART_LIST);
        getSavedData(showCart);
    }

    private void getSavedData(boolean showCart) {
        cartWishlistViewModel.books = new ArrayList<>();
        if (showCart) {
            String cartJSON = "";
            cartJSON = cartWishlistViewModel.preferenceHelper.getUserInfo(Constants.CART_DATA) != null ? cartWishlistViewModel.preferenceHelper.getUserInfo(Constants.CART_DATA) : "";
            if (cartJSON.length() > 0) {
                cartWishlistViewModel.books = cartWishlistViewModel.gson.fromJson(cartJSON, new TypeToken<List<Book>>() {
                }.getType());
            }
        } else {
            String wishListJSON = "";
            wishListJSON = cartWishlistViewModel.preferenceHelper.getUserInfo(Constants.WISHLIST_DATA) != null ? cartWishlistViewModel.preferenceHelper.getUserInfo(Constants.WISHLIST_DATA) : "";
            if (wishListJSON.length() > 0) {
                cartWishlistViewModel.books = cartWishlistViewModel.gson.fromJson(wishListJSON, new TypeToken<List<Book>>() {
                }.getType());
            }
        }
    }

    private void setupToolbar() {
        parentLayout = (RelativeLayout) cartWishlistBinding.parentLayout;
        toolbar = (Toolbar) cartWishlistBinding.toolBar;
        toolbar.setTitleMarginStart(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(showCart ? "Cart" : "Wishlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView() {

        if (cartWishlistViewModel.books.size() > 0) {
            cartWishlistViewModel.bookRecyclerView = (RecyclerView) cartWishlistBinding.cartWishlistRecyclerView;
            cartWishlistViewModel.bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cartWishlistViewModel.bookRecyclerView.setItemAnimator(new DefaultItemAnimator());

            cartWishlistViewModel.cartWishlistAdapter = new CartWishlistAdapter(this, R.layout.cart_wishlist_book_info_card, cartWishlistViewModel.books);
            cartWishlistViewModel.bookRecyclerView.setAdapter(cartWishlistViewModel.cartWishlistAdapter);

            ItemClickSupport.addTo(cartWishlistViewModel.bookRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                }
            });
        } else {
            cartWishlistViewModel.commonHelper.showSnackAlert(showCart ? getString(R.string.no_cart_data_message) : getString(R.string.no_wishlist_data_message), parentLayout);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
