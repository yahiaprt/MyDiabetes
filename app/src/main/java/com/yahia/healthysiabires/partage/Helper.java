package com.yahia.healthysiabires.partage;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.DimenRes;

import com.yahia.healthysiabires.healthysiabiresApplication;
import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.database.ydk.mesorationydk;
import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.Random;

/**
 * Created by Filip on 10.12.13.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Helper {

    private static final String TAG = Helper.class.getSimpleName();
    private static final String FORMAT_TIME = "HH:mm";

    public static Locale getLocale() {
        return healthysiabiresApplication.getContext().getResources().getConfiguration().locale;
    }

    public static boolean isSystemLocale(String languageCode) {
        return languageCode != null && isSystemLocale(new Locale(languageCode));
    }

    public static boolean isSystemLocale(Locale locale) {
        Locale systemLocale = getLocale();
        String systemLanguage = systemLocale != null ? systemLocale.getLanguage() : null;
        String language = locale != null ? locale.getLanguage() : null;
        return systemLanguage != null && language != null && systemLanguage.equals(language);
    }

    public static String getLanguageCode() {
        return getLocale().getLanguage();
    }

    public static String getCountryCode() {
        return getLocale().getCountry();
    }

    public static DateTimeFormatter getDateFormat() {
        return DateTimeFormat.mediumDate();
    }

    public static DateTimeFormatter getTimeFormat() {
        return DateTimeFormat.forPattern(FORMAT_TIME);
    }

    public static float getDPI(@DimenRes int dimenResId) {
        return healthysiabiresApplication.getContext().getResources().getDimensionPixelSize(dimenResId);
    }

    public static String getTextAgo(Context context, int differenceInMinutes) {
        if (differenceInMinutes < 2) {
            return context.getString(R.string.latest_moments);
        }

        String textAgo = context.getString(R.string.latest);

        if (differenceInMinutes > 2879) {
            differenceInMinutes = differenceInMinutes / 60 / 24;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.days));
        } else if (differenceInMinutes > 119) {
            differenceInMinutes = differenceInMinutes / 60;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.hours));
        } else {
            textAgo = textAgo.replace("[unit]", context.getString(R.string.minutes));
        }

        return textAgo.replace("[value]", Integer.toString(differenceInMinutes));
    }

    public static int colorBrighten(int color, float percent) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb((int) (r * percent), (int) (g * percent), (int) (b * percent));
    }

    public static String toStringDelimited(String[] array, char delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : array) {
            stringBuilder.append(string);
            stringBuilder.append(delimiter);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static <T extends Enum<?>> T valueOf(Class<T> enumeration, String search) {
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return each;
            }
        }
        return null;
    }

    public static <T extends mesoration> void createTestData() {
        final int entryCount = 500;
        Random random = new Random();
        for (int i = 0; i < entryCount; i++) {
            Entry entry = new Entry();
            entry.setDate(DateTime.now().minusHours(entryCount - i));
            EntryDao.getInstance().createOrUpdate(entry);
            int categoryIndex = random.nextInt(type.values().length - 1);
            type category = type.values()[categoryIndex];
            try {
                T measurement = (T) category.toClass().newInstance();
                measurement.setValues(new float[]{111});
                measurement.setEntry(entry);
                mesorationydk.getInstance(measurement.getClass()).createOrUpdate(measurement);
            } catch (InstantiationException exception) {
                Log.d(TAG, exception.getMessage());
            } catch (IllegalAccessException exception) {
                Log.d(TAG, exception.getMessage());
            }
            Log.d(TAG, String.format("Added %d/%d", i, entryCount));
        }
    }

    public static float calculateHbA1c(float avgMgDl) {
        return 0.031f * avgMgDl + 2.393f;
    }

    public static float parseKcalToKj(float kcal) {
        return kcal * 4.184f;
    }
}
