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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetup();
    }

    private void initSetup() {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        dashboardViewModel.mProgress = new ProgressDialog(this);
        dashboardViewModel.commonHelper = new CommonHelper(this);
        dashboardViewModel.preferenceHelper = new PreferenceHelper(this);
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
                                        dashboardViewModel.commonHelper.showAlert(getString(R.string.title_failed), getString(R.string.invalid_username_password_message));
                                    }
                                }
                            }
                        } catch (JSONException ex) {

                        }
                    }
                } else {
                    dashboardViewModel.commonHelper.showAlert(getString(R.string.title_failed), getString(R.string.invalid_username_password_message));
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar((Toolbar) dashboardBinding.toolBar);
        getSupportActionBar().setTitle("Home");
    }

    private void setupRecyclerView() {
        dashboardViewModel.books = new ArrayList<>();
        dashboardViewModel.bookRecyclerView = (RecyclerView) dashboardBinding.bookRecyclerView;
        dashboardViewModel.bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dashboardViewModel.bookRecyclerView.setItemAnimator(new DefaultItemAnimator());

        dashboardViewModel.bookAdapter = new BookAdapter(this, R.layout.book_info_card, dashboardViewModel.books);
        dashboardViewModel.bookRecyclerView.setAdapter(dashboardViewModel.bookAdapter);

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
            dashboardViewModel.bookLiveDataList.setValue(dashboardViewModel.books);
        } catch (JSONException ex) {

        }
    }

    @Override
    public void onBackPressed() {

    }
}
