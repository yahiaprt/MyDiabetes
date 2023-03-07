package com.mydiabetesprt.diabetes.partage.networkage;

import androidx.annotation.Nullable;

public class NetworkageResponse<T> {

    private int statusCode;

    @Nullable
    private T data;

    NetworkageResponse(int statusCode, @Nullable T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    NetworkageResponse(int statusCode) {
        this(statusCode, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Nullable
    public T getData() {
        return data;
    }
}
