package com.yahia.healthysiabires.future.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.yahia.healthysiabires.healthysiabiresApplication;
import com.yahia.healthysiabires.R;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.database.entity.glycemie;
import com.yahia.healthysiabires.partage.data.database.entity.Entry;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Minutes;

/**
 * Created by Faltenreich on 26.06.2016.
 */
public class AlarmUS {

    private static final int ALARM_ID = 34248273;

    private static Context getContext() {
        return healthysiabiresApplication.getContext();
    }

    private static AlarmManager getAlarmManager() {
        return (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    private static PendingIntent getPendingIntent() {
        Intent intent = new Intent(getContext(), AlarmBR.class);
        return PendingIntent.getBroadcast(getContext(), ALARM_ID, intent, 0);
    }

    public static void setAlarm(long intervalInMillis) {
        long alarmStartInMillis = System.currentTimeMillis() + intervalInMillis;
        PreferenceHelper.getInstance().setAlarmStartInMillis(alarmStartInMillis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getAlarmManager().setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    alarmStartInMillis,
                    getPendingIntent());
        } else {
            getAlarmManager().set(AlarmManager.RTC_WAKEUP,
                    alarmStartInMillis,
                    getPendingIntent());
        }
    }

    public static long getAlarmInMillis() {
        return PreferenceHelper.getInstance().getAlarmStartInMillis();
    }

    public static void stopAlarm() {
        PreferenceHelper.getInstance().setAlarmStartInMillis(-1);
        getAlarmManager().cancel(getPendingIntent());
    }

    public static boolean isAlarmSet() {
        return new DateTime(PreferenceHelper.getInstance().getAlarmStartInMillis()).isAfterNow();
    }

    static void executeAlarm(Context context) {
        PreferenceHelper.getInstance().setAlarmStartInMillis(-1);
        NotificationUS.showNotification(context, R.string.alarm, getMessageForMeasurement());
    }

    private static String getMessageForMeasurement() {
        Context context = healthysiabiresApplication.getContext();
        Entry lastMeasurement = EntryDao.getInstance().getLatestWithMeasurement(glycemie.class);
        String message = context.getString(R.string.alarm_desc_first);
        if (lastMeasurement != null) {
            // Calculate how long the last measurement has been ago
            Interval interval = new Interval(lastMeasurement.getDate(), DateTime.now());
            if (Minutes.minutesIn(interval).getMinutes() < 120) {
                message = String.format(
                        context.getString(R.string.alarm_desc),
                        Integer.toString(Minutes.minutesIn(interval).getMinutes()) + " " +
                                context.getString(R.string.minutes));
            } else {
                message = String.format(
                        context.getString(R.string.alarm_desc),
                        Integer.toString(Hours.hoursIn(interval).getHours()) + " " +
                                context.getString(R.string.hours));
            }
        }
        return message;
    }
}
