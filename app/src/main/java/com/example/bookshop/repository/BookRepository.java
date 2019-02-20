package com.example.bookshop.repository;

import com.example.bookshop.common.MethodTags;
import com.example.bookshop.networking.ApiClient;
import com.example.bookshop.networking.ApiEndPoints;
import com.example.bookshop.utils.ResponseCallback;

public class BookRepository {

    ApiClient apiClient;

    public BookRepository(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void loadBooks(final ResponseCallback responseCallback) {

        String url = ApiEndPoints.BASE_URL + ApiEndPoints.BOOK_LIST_URL;
        apiClient.get(url, MethodTags.ONE, new ResponseCallback() {
            @Override
            public void responseHandler(String response, int tag, int statusCode) {
                responseCallback.responseHandler(response, tag, statusCode);
            }
        });
    }
}
