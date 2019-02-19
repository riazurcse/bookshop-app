package com.example.bookshop.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.bookshop.R;
import com.example.bookshop.databinding.ActivityLoginBinding;
import com.example.bookshop.model.User;
import com.example.bookshop.viewmodel.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener {


    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    Toolbar toolbar;

    boolean passwordVisibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetup();
    }

    private void initSetup() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setupToolbar();
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
                else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    binding.passwordET.setError("Enter at least 6 digit password");
                    binding.passwordET.requestFocus();
                }
                else {
                    binding.usernameET.setText(loginUser.getEmailAddress());
                    binding.passwordET.setText(loginUser.getPassword());
                    prepareLoginData(binding.usernameET.getText().toString(), binding.passwordET.getText().toString());
                }
            }
        });
        binding.passwordET.setOnTouchListener(this);
        loginViewModel.getResponse().observe(this, new Observer<String>(){

            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    Log.d("TAG", "");
                }

            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar((Toolbar) binding.toolBar);
        getSupportActionBar().setTitle("Login");
    }

    private void prepareLoginData(String username, String password) {
        try {
            JSONObject params = new JSONObject();
            params.put("email", username);
            params.put("password", password);
            loginViewModel.doLogin(params);

        } catch (JSONException ex) {

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if(motionEvent.getRawX() >= (binding.passwordET.getRight() - binding.passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                int start, end;
                start = binding.passwordET.getSelectionStart();
                end = binding.passwordET.getSelectionEnd();
                if (!passwordVisibility) {
                    passwordVisibility = true;
                    binding.passwordET.setTransformationMethod(null);
                    binding.passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                    binding.passwordET.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.passwordET.setSelection(start, end);
                }
                else {
                    passwordVisibility = false;
                    binding.passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                    binding.passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.passwordET.setSelection(start, end);
                }
                return true;
            }
        }
        return false;
    }
}
