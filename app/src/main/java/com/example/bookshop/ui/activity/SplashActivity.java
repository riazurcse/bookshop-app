package com.example.bookshop.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.bookshop.R;
import com.example.bookshop.common.Constants;
import com.example.bookshop.utils.PreferenceHelper;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static int SPLASH_TIME_OUT = 5000;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initHelpers();
        splashTimeOut();
    }

    private void initHelpers() {
        preferenceHelper = new PreferenceHelper(this);
    }

    private void splashTimeOut() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                boolean isLoggedIn = preferenceHelper.getBooleanValue(Constants.LOGIN_STATUS);
                if (isLoggedIn) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
