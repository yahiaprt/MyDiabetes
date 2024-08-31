package com.yahia.healthysiabires;

import android.app.Application;
import android.content.Context;

import com.yahia.healthysiabires.partage.data.database.ImportanceHelper;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.Database;
import com.yahia.healthysiabires.partage.data.database.migration.Migrateur;
import com.yahia.healthysiabires.partage.view.image.ImageLoader;
import com.yahia.healthysiabires.future.alarm.NotificationUS;
import com.yahia.healthysiabires.partage.view.theme.Theme;
import com.yahia.healthysiabires.partage.view.theme.ThemeUs;

import net.danlew.android.joda.JodaTimeAndroid;

public class healthysiabiresApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        init();


    }

    public static Context getContext() {
        return context;
    }

    private void init() {
        context = getApplicationContext();
        JodaTimeAndroid.init(this);
        PreferenceHelper.getInstance().init(this);
        Database.getInstance().init(this);
        ImportanceHelper.validateImports(this);
       //LicenseResolver.registerLicense(new OpenDatabaseLicense());
        PreferenceHelper.getInstance().migrate();
        Migrateur.getInstance().start(this);
        NotificationUS.setupNotifications(this);
        Theme theme = PreferenceHelper.getInstance().getTheme();
        ThemeUs.setDefaultNightMode(theme);
        ThemeUs.setUiMode(this, theme);
        ImageLoader.getInstance().init(this);
    }
}