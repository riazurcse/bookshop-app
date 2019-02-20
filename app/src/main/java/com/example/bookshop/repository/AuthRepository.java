package com.example.bookshop.repository;

import com.example.bookshop.common.MethodTags;
import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.networking.ApiEndPoints;
import com.example.bookshop.utils.ResponseCallback;

import org.json.JSONObject;


public class AuthRepository {

    ApiClient apiClient;

    public AuthRepository(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void doLogin(JSONObject params, final ResponseCallback responseCallback) {
        String url = ApiEndPoints.BASE_URL + ApiEndPoints.LOGIN_URL;
        apiClient.post(url, params, MethodTags.ONE, new ResponseCallback() {
            @Override
            public void responseHandler(String response, int tag, int statusCode) {
                responseCallback.responseHandler(response, tag, statusCode);
            }
        });
    }

    public void doSignup(JSONObject params, final ResponseCallback responseCallback) {

        String url = ApiEndPoints.BASE_URL + ApiEndPoints.SIGNUP_URL;
        apiClient.post(url, params, MethodTags.ONE, new ResponseCallback() {
            @Override
            public void responseHandler(String response, int tag, int statusCode) {
                responseCallback.responseHandler(response, tag, statusCode);
            }
        });
    }
}
