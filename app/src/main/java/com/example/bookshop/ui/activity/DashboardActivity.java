package com.example.bookshop.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.bookshop.R;
import com.example.bookshop.common.Constants;
import com.example.bookshop.databinding.ActivityDashboardBinding;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Response;
import com.example.bookshop.utils.CommonHelper;
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

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetup();
    }

    private void initSetup() {
        preferenceHelper = new PreferenceHelper(this);

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        dashboardViewModel.mProgress = new ProgressDialog(this);
        dashboardViewModel.commonHelper = new CommonHelper(this);
        dashboardViewModel.loadBooks();
        setupToolbar();
        dashboardBinding.setLifecycleOwner(this);
        dashboardBinding.setDashboardViewModel(dashboardViewModel);
        dashboardViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                Log.d(TAG, "got book list");
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
        getSupportActionBar().setTitle("Dashboard");
    }

    private void prepareBookList(JSONArray bookJSONArray) {

        try {
            List<Book> books = new ArrayList<>();
            for (int i = 0; i < bookJSONArray.length(); i++) {
                JSONObject bookJSON = bookJSONArray.getJSONObject(i);
                if (bookJSON != null) {
                    Gson gson = new Gson();
                    Book book = gson.fromJson(bookJSON.toString(), Book.class);
                    books.add(book);
                }
            }
            dashboardViewModel.bookLiveDataList.setValue(books);
        } catch (JSONException ex) {

        }
    }

    @Override
    public void onBackPressed() {

    }
}
