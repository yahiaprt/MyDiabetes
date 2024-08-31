package com.yahia.healthysiabires.partage.data.synchronisation;

public interface DataLoaderListener<T> {
    T onShouldLoad();
    void onDidLoad(T data);
}
