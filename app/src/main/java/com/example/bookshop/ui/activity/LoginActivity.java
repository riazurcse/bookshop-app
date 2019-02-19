package com.example.bookshop.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.example.bookshop.R;
import com.example.bookshop.databinding.ActivityLoginBinding;
import com.example.bookshop.model.User;
import com.example.bookshop.viewmodel.LoginViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener {


    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initSetup();
    }

    private void initSetup() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);
        loginViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User loginUser) {

                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getEmailAddress())) {
                    binding.usernameET.setError("Enter an E-Mail Address");
                    binding.usernameET.requestFocus();
                }
                else if (!loginUser.isEmailValid()) {
                    binding.usernameET.setError("Enter a Valid E-mail Address");
                    binding.usernameET.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPassword())) {
                    binding.passwordET.setError("Enter a Password");
                    binding.passwordET.requestFocus();
                }
                else if (!loginUser.isPasswordLengthGreaterThan8()) {
                    binding.passwordET.setError("Enter at least 8 Digit password");
                    binding.passwordET.requestFocus();
                }
                else {
                    binding.usernameET.setText(loginUser.getEmailAddress());
                    binding.passwordET.setText(loginUser.getPassword());
                }
            }
        });
        binding.passwordET.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if(motionEvent.getRawX() >= (binding.passwordET.getRight() - binding.passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here

                return true;
            }
        }
        return false;
    }
}
