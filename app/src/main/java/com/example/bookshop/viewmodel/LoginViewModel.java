package com.example.bookshop.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.View;

import com.example.bookshop.model.User;
import com.example.bookshop.repository.AuthRepository;

import org.json.JSONObject;

public class LoginViewModel extends ViewModel {

    private AuthRepository authRepository;

    public LoginViewModel() {
        authRepository = new AuthRepository();
    }

    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<String> response;

    public MutableLiveData<User> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public MutableLiveData<String> getResponse() {
        if (response == null) {
            response = new MutableLiveData<>();
        }
        return response;
    }

    public void onClick(View view) {

        User loginUser = new User(emailAddress.getValue(), password.getValue());
        userMutableLiveData.setValue(loginUser);
    }

    public void doLogin(JSONObject params) {
        authRepository.doLogin(params);
        //response.setValue(value.getValue());
    }
}
