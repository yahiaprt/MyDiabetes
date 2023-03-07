package com.mydiabetesprt.diabetes.partage.event;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Faltenreich on 01.01.2016.
 */
public class ets {

    private static final String TAG = ets.class.getSimpleName();

    private static EventBus getEventBus() {
        return EventBus.getDefault();
    }

    public static void register(Object object) {
        if (!isRegistered(object)) {
            Log.d(TAG, "Registered " + object.getClass().getSimpleName());
            getEventBus().register(object);
        }
    }

    public static void unregister(Object object) {
        if (isRegistered(object)) {
            Log.d(TAG, "Unregistered " + object.getClass().getSimpleName());
            getEventBus().unregister(object);
        }
    }

    private static boolean isRegistered(Object object) {
        return getEventBus().isRegistered(object);
    }

    public static void post(BaseEt event) {
        Log.d(TAG, "Posted " + event.getClass().getSimpleName());
        getEventBus().post(event);
    }
}
