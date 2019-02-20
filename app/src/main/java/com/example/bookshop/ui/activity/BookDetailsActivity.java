package com.example.bookshop.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookshop.R;
import com.example.bookshop.common.IntentKeys;
import com.example.bookshop.databinding.ActivityBookDetailsBinding;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.PreferenceHelper;
import com.example.bookshop.viewmodel.DetailsViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class BookDetailsActivity extends AppCompatActivity {

    private static final String TAG = BookDetailsActivity.class.getName();

    private DetailsViewModel detailsViewModel;
    private ActivityBookDetailsBinding bookDetailBinding;

    Toolbar toolbar;

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
        setupToolbar();
        bookDetailBinding.setLifecycleOwner(this);
    }

    private void dataReceived() {
        Bundle bundle = getIntent().getExtras();
        String bookDetails = bundle.getString(IntentKeys.BOOK_DETAILS);
        Gson gson = new Gson();
        detailsViewModel.book = gson.fromJson(bookDetails, Book.class);
        ImageView preview = (ImageView) bookDetailBinding.bookDetailPreviewIV;
        if (detailsViewModel.book.getPreview().length() > 0) {
            String previewURL = detailsViewModel.book.getPreview();
            Glide.with(this).load(previewURL).placeholder(R.drawable.ic_placeholder).into(preview);
        }
    }

    private void setupToolbar() {
        toolbar = (Toolbar) bookDetailBinding.toolBar;
        toolbar.setTitleMarginStart(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
