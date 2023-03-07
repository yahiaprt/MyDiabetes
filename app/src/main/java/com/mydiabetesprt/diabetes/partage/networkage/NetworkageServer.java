package com.mydiabetesprt.diabetes.partage.networkage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

abstract public class NetworkageServer<API> {

    public API api;

    public NetworkageServer(Class<API> clazz) {
        this.api = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(clazz);
    }

    public abstract String getBaseUrl();
}
