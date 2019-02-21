package com.example.bookshop.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.bookshop.R;
import com.example.bookshop.common.Constants;
import com.example.bookshop.common.IntentKeys;
import com.example.bookshop.databinding.ActivityDashboardBinding;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Response;
import com.example.bookshop.ui.adapter.BookAdapter;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.ItemClickSupport;
import com.example.bookshop.utils.PreferenceHelper;
import com.example.bookshop.viewmodel.DashboardViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getName();

    private DashboardViewModel dashboardViewModel;
    private ActivityDashboardBinding dashboardBinding;

    Toolbar toolbar;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetup();
        setupNavigationDrawer(savedInstanceState);
    }

    private void initSetup() {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        dashboardViewModel.mProgress = new ProgressDialog(this);
        dashboardViewModel.commonHelper = new CommonHelper(this);
        dashboardViewModel.preferenceHelper = new PreferenceHelper(this);
        dashboardViewModel.gson = new Gson();
        setupToolbar();
        setupRecyclerView();
        dashboardViewModel.loadBooks();
        dashboardBinding.setLifecycleOwner(this);
        dashboardBinding.setDashboardViewModel(dashboardViewModel);
        dashboardViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                if (books != null) {
                    dashboardViewModel.bookAdapter = new BookAdapter(DashboardActivity.this, R.layout.book_info_card, books);
                    dashboardViewModel.bookRecyclerView.setAdapter(dashboardViewModel.bookAdapter);
                }
            }
        });

        dashboardViewModel.getResponse().observe(this, new Observer<Response>() {

            @Override
            public void onChanged(@Nullable Response response) {
                dashboardViewModel.mProgress.dismiss();
                if (response != null) {
                    if (response.getStatusCode() == Constants.STATUS_OK) {
                        try {
                            JSONObject responseJSON = new JSONObject(response.getResponse());
                            if (responseJSON != null) {
                                if (!responseJSON.isNull("error")) {
                                    boolean error = responseJSON.getBoolean("error");
                                    if (!error) {
                                        if (!responseJSON.isNull("data")) {
                                            JSONArray bookJSONArray = responseJSON.getJSONArray("data");
                                            prepareBookList(bookJSONArray);
                                        }
                                    } else {
                                        dashboardViewModel.commonHelper.showAlert("" + response.getStatusCode(), getString(R.string.something_went_wrong_text));
                                    }
                                }
                            }
                            else {
                                dashboardViewModel.commonHelper.showAlert("" + response.getStatusCode(), getString(R.string.something_went_wrong_text));
                            }
                        } catch (JSONException ex) {

                        }
                    }
                    else {
                        dashboardViewModel.commonHelper.showAlert("" + response.getStatusCode(), getString(R.string.something_went_wrong_text));
                    }
                } else {
                    dashboardViewModel.commonHelper.showAlert("" + response.getStatusCode(), getString(R.string.something_went_wrong_text));
                }
            }
        });
    }

    private void setupToolbar() {
        toolbar = (Toolbar) dashboardBinding.toolBar;
        toolbar.setTitleMarginStart(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
    }

    private void setupNavigationDrawer(Bundle savedInstanceState) {

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Cart").withIcon(R.drawable.ic_cart).withIdentifier(1).withSelectable(true),
                        new PrimaryDrawerItem().withName("Wishlist").withIcon(R.drawable.ic_wishlist).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.ic_logout).withIdentifier(3).withSelectable(true)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(DashboardActivity.this, CartWishlistActivity.class);
                                intent.putExtra(IntentKeys.SHOW_CART_LIST, true);
                                startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(DashboardActivity.this, CartWishlistActivity.class);
                                intent.putExtra(IntentKeys.SHOW_CART_LIST, false);
                                startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 3) {
                                dashboardViewModel.preferenceHelper.removeAll();
                                intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                finishAffinity();
                                startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void getCachedData() {
        dashboardViewModel.books = new ArrayList<>();
        String cachedJSON = "";
        cachedJSON = dashboardViewModel.preferenceHelper.getUserInfo(Constants.CACHE_DATA) != null ? dashboardViewModel.preferenceHelper.getUserInfo(Constants.CACHE_DATA) : "";
        if (cachedJSON.length() > 0) {
            dashboardViewModel.books = dashboardViewModel.gson.fromJson(cachedJSON, new TypeToken<List<Book>>() {
            }.getType());
        }
    }

    private void setupRecyclerView() {
        getCachedData();
        dashboardViewModel.bookRecyclerView = (RecyclerView) dashboardBinding.bookRecyclerView;
        dashboardViewModel.bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dashboardViewModel.bookRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if(dashboardViewModel.books.size() > 0) {
            dashboardViewModel.bookAdapter = new BookAdapter(this, R.layout.book_info_card, dashboardViewModel.books);
            dashboardViewModel.bookRecyclerView.setAdapter(dashboardViewModel.bookAdapter);
        }

        ItemClickSupport.addTo(dashboardViewModel.bookRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Gson gson = new Gson();
                String detailJSON = gson.toJson(dashboardViewModel.books.get(position));
                Intent intent = new Intent(DashboardActivity.this, BookDetailsActivity.class);
                intent.putExtra(IntentKeys.BOOK_DETAILS, detailJSON);
                startActivity(intent);
            }
        });
    }

    private void prepareBookList(JSONArray bookJSONArray) {

        try {
            dashboardViewModel.books = new ArrayList<>();
            for (int i = 0; i < bookJSONArray.length(); i++) {
                JSONObject bookJSON = bookJSONArray.getJSONObject(i);
                if (bookJSON != null) {
                    Gson gson = new Gson();
                    Book book = gson.fromJson(bookJSON.toString(), Book.class);
                    dashboardViewModel.books.add(book);
                }
            }
            String cachedJSON = "";
            if (dashboardViewModel.books.size() > 0) {
                cachedJSON = dashboardViewModel.gson.toJson(dashboardViewModel.books, new TypeToken<List<Book>>(){}.getType());
                dashboardViewModel.preferenceHelper.saveUserInfo(Constants.CACHE_DATA, cachedJSON);
                dashboardViewModel.bookLiveDataList.setValue(dashboardViewModel.books);
            }
        } catch (JSONException ex) {

        }
    }

    @Override
    public void onBackPressed() {

    }
}
