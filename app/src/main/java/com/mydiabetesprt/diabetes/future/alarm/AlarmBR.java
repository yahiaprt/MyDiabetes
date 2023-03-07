package com.mydiabetesprt.diabetes.future.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Filip on 22.12.2014.
 */

 public class AlarmBR extends BroadcastReceiver {









    private static final String TAG = AlarmBR.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent for BroadcastReceiver");
        AlarmUS.executeAlarm(context);
    }
}
