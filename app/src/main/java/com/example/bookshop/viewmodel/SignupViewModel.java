package com.example.bookshop.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.example.bookshop.model.Response;
import com.example.bookshop.model.UserSignup;
import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.repository.AuthRepository;

public class SignupViewModel extends ViewModel {

    private AuthRepository authRepository;
    private ApiClient apiClient;

    public SignupViewModel() {
        apiClient = new ApiClient();
        authRepository = new AuthRepository(apiClient);
    }

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<UserSignup> userMutableLiveData;
    private MutableLiveData<Response> response;

    public MutableLiveData<UserSignup> getSignupUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public MutableLiveData<Response> getResponse() {
        if (response == null) {
            response = new MutableLiveData<>();
        }
        return response;
    }

    public void onClick(View view) {

        UserSignup signupUser = new UserSignup(name.getValue(), emailAddress.getValue(), password.getValue());
        userMutableLiveData.setValue(signupUser);
    }
}
