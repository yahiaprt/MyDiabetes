package com.mydiabetesprt.diabetes.partage.networkage;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkageService<SERVER> {

    private static final String TAG = NetworkageService.class.getSimpleName();

    protected SERVER server;

    public NetworkageService(SERVER server) {
        this.server = server;
    }

    protected <T> void execute(Call<T> call, final NetworkResponseListener<T> listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                Log.d(TAG, "Request succeeded with: " + response);
                listener.onResponse(new NetworkageResponse<>(response.code(), response.body()));
            }
            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
                Log.d(TAG, "Request failed with: " + throwable);
                int statusCode;
                if (throwable instanceof SocketTimeoutException) {
                    statusCode = HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
                } else if (throwable instanceof IOException) {
                    statusCode = HttpURLConnection.HTTP_NOT_FOUND;
                } else {
                    statusCode = HttpURLConnection.HTTP_NOT_ACCEPTABLE;
                }
                listener.onError(new NetworkageResponse<T>(statusCode));
            }
        });
    }

    public interface NetworkResponseListener<T> {
        void onResponse(NetworkageResponse<T> response);
        void onError(NetworkageResponse<T> response);
    }
}
