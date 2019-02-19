package com.example.bookshop.networking;

import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.bookshop.common.Constants;
import com.example.bookshop.repository.AuthRepository;
import com.example.bookshop.utils.ResponseCallback;

import org.json.JSONObject;

public class ApiClient {


    Context context;
    private TaskListener taskListener;
    private ResponseCallback responseCallback;
    ProgressDialog mProgress;
    //CommonService commonService;
    //PreferenceHelper preferenceHelper;
    String loadingMessage = "Loading...";

    public ApiClient() {

    }

    public ApiClient(AuthRepository authRepository) {
        this.responseCallback = (ResponseCallback) authRepository;
    }

    public void registerCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    public ApiClient(Context context) {
        this.context = context;
        //taskListener = (TaskListener) context;
        //commonService = new CommonService(context);
        //preferenceHelper = new PreferenceHelper(context);
    }

    public ApiClient(Context context, TaskListener taskListener) {
        this.context = context;
        this.taskListener = taskListener;
        //commonService = new CommonService(context);
        //preferenceHelper = new PreferenceHelper(context);
    }

    public MutableLiveData<String> get(String url, final int TAG, boolean isLoaderCancelable) {
        //showLoader(isLoaderCancelable, loadingMessage);
        final MutableLiveData<String> getResponse = new MutableLiveData<>();
        AndroidNetworking.get(url)
                //.addHeaders(getHeaders())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //mProgress.dismiss();
                        //taskListener.taskCompleted(response.toString(), TAG, Constants.STATUS_OK);
                        getResponse.setValue(response.toString());
                        responseCallback.responseHandler(response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        //mProgress.dismiss();
                        int code = error.getErrorCode();
                        //taskListener.taskCompleted(error.toString(), TAG, code);
                        getResponse.setValue(error.getErrorBody());
                        responseCallback.responseHandler(error.getErrorBody());
                    }
                });
        return getResponse;
    }

    public void get(String url, final int TAG) {
        //showLoader(isLoaderCancelable, loadingMessage);
        AndroidNetworking.get(url)
                //.addHeaders(getHeaders())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseCallback.responseHandler(response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        //mProgress.dismiss();
                        int code = error.getErrorCode();
                        responseCallback.responseHandler(error.getErrorBody());
                    }
                });
    }



    public void post(String url, JSONObject params, final int TAG, boolean isLoaderCancelable) {
        final MutableLiveData<String> postResponse = new MutableLiveData<>();
        //showLoader(isLoaderCancelable, loadingMessage);
        AndroidNetworking.post(url)
                //.addHeaders(getHeaders())
                .addJSONObjectBody(params)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                       //mProgress.dismiss();
                        //taskListener.taskCompleted(response.toString(), TAG, Constants.STATUS_OK);
                        postResponse.setValue(response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                       // mProgress.dismiss();
                        int code = error.getErrorCode();
                        //taskListener.taskCompleted(error.toString(), TAG, code);
                    }
                });
       // return postResponse;
    }


    public void showLoader(boolean isCancelable, String message) {
        mProgress = new ProgressDialog(context);
        mProgress.setMessage(message);
        if (!isCancelable) {
            mProgress.setCancelable(false);
        } else {
            mProgress.setCancelable(true);
        }
        mProgress.show();
    }

//    private Map<String, String> getHeaders() {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", getBearerToken());
//        headers.put("Content-Type", "application/json");
//        return headers;
//    }

}
