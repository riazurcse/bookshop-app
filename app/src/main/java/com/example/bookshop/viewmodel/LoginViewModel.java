package com.example.bookshop.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.example.bookshop.model.Response;
import com.example.bookshop.model.User;
import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.repository.AuthRepository;
import com.example.bookshop.utils.ResponseCallback;

import org.json.JSONObject;

public class LoginViewModel extends ViewModel {

    private AuthRepository authRepository;
    private ApiClient apiClient;

    public LoginViewModel() {
        apiClient = new ApiClient();
        authRepository = new AuthRepository(apiClient);
    }

    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Boolean> signupTextViewClicked = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Response> response;

    public MutableLiveData<User> getUser() {

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

        User loginUser = new User(emailAddress.getValue(), password.getValue());
        userMutableLiveData.setValue(loginUser);
    }

    public void signupClicked(View view) {
        signupTextViewClicked.setValue(true);
    }

    public void doLogin(JSONObject params) {
        authRepository.doLogin(params, new ResponseCallback() {
            @Override
            public void responseHandler(String res, int tag, int statusCode) {
                Response apiResponse = new Response(statusCode, res);
                response.setValue(apiResponse);
            }
        });
    }
}
