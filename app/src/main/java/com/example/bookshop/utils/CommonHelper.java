package com.example.bookshop.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class CommonHelper {

    private Context mContext;

    public CommonHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void showLoader(ProgressDialog mProgress, boolean isCancelable, String message) {
        mProgress.setMessage(message);
        if (!isCancelable) {
            mProgress.setCancelable(false);
        } else {
            mProgress.setCancelable(true);
        }
        mProgress.show();
    }
}
