package com.mydiabetesprt.diabetes.partage.data.synchronisation;

import android.content.Context;

public class DataLoader {

    private static DataLoader instance;

    public static DataLoader getInstance() {
        if (instance == null) {
            instance = new DataLoader();
        }
        return instance;
    }

    public <T> void load(Context context, DataLoaderListener<T> listener) {
        new DataTask<>(context, listener).execute();
    }
}
