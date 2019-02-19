package com.example.bookshop.networking;

public interface TaskListener {
    public void taskCompleted(String result, int TAG, int statusCode);
}
