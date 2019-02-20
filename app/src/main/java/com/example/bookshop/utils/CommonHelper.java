package com.example.bookshop.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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

    public void showAlert(String title, String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
