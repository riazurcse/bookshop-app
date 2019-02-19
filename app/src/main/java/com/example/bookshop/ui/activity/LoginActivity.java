package com.example.bookshop.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.bookshop.R;
import com.example.bookshop.databinding.ActivityLoginBinding;
import com.example.bookshop.model.User;
import com.example.bookshop.viewmodel.LoginViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


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
    }
}
