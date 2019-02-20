package com.example.bookshop.networking;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.bookshop.common.Constants;
import com.example.bookshop.utils.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {

    public ApiClient() {

    }

    public void get(String url, final int TAG, final ResponseCallback callback) {

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.responseHandler(response.toString(), TAG, Constants.STATUS_OK);
                    }

                    @Override
                    public void onError(ANError error) {
                        callback.responseHandler(error.getLocalizedMessage(), TAG, error.getErrorCode());
                    }
                });
    }


    public void post(String url, JSONObject params, final int TAG, final ResponseCallback callback) {

        AndroidNetworking.post(url)
                .addJSONObjectBody(params)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.responseHandler(response.toString(), TAG, Constants.STATUS_OK);
                    }

                    @Override
                    public void onError(ANError error) {
                        callback.responseHandler(error.getLocalizedMessage(), TAG, error.getErrorCode());
                    }
                });
    }
}
