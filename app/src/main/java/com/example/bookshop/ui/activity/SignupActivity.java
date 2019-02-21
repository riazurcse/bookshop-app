package com.example.bookshop.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.example.bookshop.common.Constants;
import com.example.bookshop.databinding.ActivitySignupBinding;
import com.example.bookshop.model.Response;
import com.example.bookshop.model.UserSignup;
import com.example.bookshop.utils.CommonHelper;
import com.example.bookshop.utils.PreferenceHelper;
import com.example.bookshop.viewmodel.SignupViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = SignupActivity.class.getName();

    private SignupViewModel signupViewModel;
    private ActivitySignupBinding signupBinding;

    Toolbar toolbar;

    boolean passwordVisibility = false;
    ProgressDialog mProgress;
    private CommonHelper commonHelper;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetup();
    }


    private void initSetup() {
        mProgress = new ProgressDialog(this);
        commonHelper = new CommonHelper(this);
        preferenceHelper = new PreferenceHelper(this);

        signupViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        signupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        setupToolbar();
        signupBinding.setLifecycleOwner(this);
        signupBinding.setSignupViewModel(signupViewModel);
        signupViewModel.getSignupUser().observe(this, new Observer<UserSignup>() {
            @Override
            public void onChanged(@Nullable UserSignup userSignup) {

                if (TextUtils.isEmpty(Objects.requireNonNull(userSignup).getName())) {
                    signupBinding.nameET.setError("Enter a Name");
                    signupBinding.nameET.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(userSignup).getEmail())) {
                    signupBinding.signupUsernameET.setError("Enter an E-Mail Address");
                    signupBinding.signupUsernameET.requestFocus();
                } else if (!userSignup.isEmailValid()) {
                    signupBinding.signupUsernameET.setError("Enter a Valid E-mail Address");
                    signupBinding.signupUsernameET.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(userSignup).getPassword())) {
                    signupBinding.signupPasswordET.setError("Enter a Password");
                    signupBinding.signupPasswordET.requestFocus();
                } else if (!userSignup.isPasswordLengthGreaterThan5()) {
                    signupBinding.signupPasswordET.setError("Enter at least 6 digit password");
                    signupBinding.signupPasswordET.requestFocus();
                } else {
                    signupBinding.nameET.setText(userSignup.getName());
                    signupBinding.signupUsernameET.setText(userSignup.getEmail());
                    signupBinding.signupPasswordET.setText(userSignup.getPassword());
                    prepareSignupData(signupBinding.nameET.getText().toString(), signupBinding.signupUsernameET.getText().toString(), signupBinding.signupPasswordET.getText().toString());
                }
            }
        });
        signupBinding.signupPasswordET.setOnTouchListener(this);
        signupViewModel.getResponse().observe(this, new Observer<Response>() {

            @Override
            public void onChanged(@Nullable Response response) {
                mProgress.dismiss();
                if (response != null) {
                    if (response.getStatusCode() == Constants.STATUS_OK) {
                        try {
                            JSONObject responseJSON = new JSONObject(response.getResponse());
                            if (responseJSON != null) {
                                if (!responseJSON.isNull("error")) {
                                    boolean error = responseJSON.getBoolean("error");
                                    String message = responseJSON.isNull("message") ? "" : responseJSON.getString("message");
                                    if (!error) {
                                        commonHelper.showAlert(getString(R.string.title_success), message);
                                        navigateToDashboard();
                                    } else {
                                        commonHelper.showAlert(getString(R.string.title_failed), message);
                                    }
                                }
                            }
                        } catch (JSONException ex) {

                        }
                    }
                    else {
                        commonHelper.showAlert("" + response.getStatusCode(), getString(R.string.something_went_wrong_text));
                    }
                } else {
                    commonHelper.showAlert("" + response.getStatusCode(), getString(R.string.something_went_wrong_text));
                }
            }
        });
    }

    private void navigateToDashboard() {

        preferenceHelper.saveUserInfo(Constants.LOGIN_STATUS, true);
        Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) signupBinding.toolBar;
        toolbar.setTitleMarginStart(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Signup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void prepareSignupData(String name, String username, String password) {
        try {
            commonHelper.showLoader(mProgress, false, Constants.LOADING);
            JSONObject params = new JSONObject();
            params.put("name", name);
            params.put("email", username);
            params.put("password", password);
            signupViewModel.doSignup(params);

        } catch (JSONException ex) {

        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final int DRAWABLE_RIGHT = 2;

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (motionEvent.getRawX() >= (signupBinding.signupPasswordET.getRight() - signupBinding.signupPasswordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                int start, end;
                start = signupBinding.signupPasswordET.getSelectionStart();
                end = signupBinding.signupPasswordET.getSelectionEnd();
                if (!passwordVisibility) {
                    passwordVisibility = true;
                    signupBinding.signupPasswordET.setTransformationMethod(null);
                    signupBinding.signupPasswordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                    signupBinding.signupPasswordET.setInputType(InputType.TYPE_CLASS_TEXT);
                    signupBinding.signupPasswordET.setSelection(start, end);
                } else {
                    passwordVisibility = false;
                    signupBinding.signupPasswordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                    signupBinding.signupPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    signupBinding.signupPasswordET.setSelection(start, end);
                }
                return true;
            }
        }
        return false;
    }
}
