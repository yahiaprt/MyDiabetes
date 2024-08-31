package com.yahia.healthysiabires.partage.event;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public abstract class BaseContextEt<T> extends BaseEt {

    public T context;

    public BaseContextEt(T context) {
        this.context = context;
    }
}
