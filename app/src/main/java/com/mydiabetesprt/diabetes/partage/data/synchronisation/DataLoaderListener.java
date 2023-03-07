package com.mydiabetesprt.diabetes.partage.data.synchronisation;

public interface DataLoaderListener<T> {
    T onShouldLoad();
    void onDidLoad(T data);
}
