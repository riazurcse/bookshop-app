package com.example.bookshop.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

public class ActivityObserver implements LifecycleObserver {

    final String TAG = this.getClass().getSimpleName().toString();
    private Context mContext;
    private OnResumeCallback onResumeCallback;

    public ActivityObserver(Context mContext) {
        this.mContext = mContext;
        onResumeCallback = (OnResumeCallback) mContext;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        onResumeCallback.onResumed(true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }
}
