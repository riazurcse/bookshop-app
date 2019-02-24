package com.example.bookshop.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;
import android.widget.Toast;

import com.example.bookshop.config.MyApp;
import com.example.bookshop.model.Response;
import com.example.bookshop.model.UserSignup;
import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.repository.AuthRepository;
import com.example.bookshop.utils.InternetConnection;
import com.example.bookshop.utils.ResponseCallback;

import org.json.JSONObject;

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

        if (InternetConnection.isConnected()) {
            UserSignup signupUser = new UserSignup(name.getValue(), emailAddress.getValue(), password.getValue());
            userMutableLiveData.setValue(signupUser);
        } else {
            Toast.makeText(MyApp.getAppContext(), "No internet connection available", Toast.LENGTH_SHORT).show();
        }
    }

    public void doSignup(JSONObject params) {

        authRepository.doSignup(params, new ResponseCallback() {
            @Override
            public void responseHandler(String res, int tag, int statusCode) {
                Response apiResponse = new Response(statusCode, res == null ? "" : res);
                response.setValue(apiResponse);
            }
        });
    }
}
