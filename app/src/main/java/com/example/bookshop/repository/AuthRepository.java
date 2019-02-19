package com.example.bookshop.repository;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.bookshop.common.MethodTags;
import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.networking.ApiEndPoints;
import com.example.bookshop.utils.ResponseCallback;

import org.json.JSONObject;



public class AuthRepository implements ResponseCallback{

    Context mContext;
    ApiClient apiClient;

    public AuthRepository() {
        apiClient = new ApiClient(this);
    }

    public void doLogin(JSONObject params) {
        //String url = ApiEndPoints.BASE_URL + ApiEndPoints.LOGIN_URL;
        String url = "https://jsonplaceholder.typicode.com/posts";
        //apiClient.post(url, params, MethodTags.ONE, false);
        apiClient.get(url, MethodTags.ONE);
    }

    @Override
    public void responseHandler(String response) {
        if (response != null) {
            Log.d("TAG", "TEST");
        }
    }
}
