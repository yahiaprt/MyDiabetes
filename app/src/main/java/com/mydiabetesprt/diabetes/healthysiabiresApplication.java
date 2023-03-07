package com.mydiabetesprt.diabetes;

import android.app.Application;
import android.content.Context;

import com.mydiabetesprt.diabetes.partage.data.database.ImportanceHelper;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.Database;
import com.mydiabetesprt.diabetes.partage.data.database.migration.Migrateur;
import com.mydiabetesprt.diabetes.partage.view.image.ImageLoader;
import com.mydiabetesprt.diabetes.future.alarm.NotificationUS;
import com.mydiabetesprt.diabetes.partage.view.theme.Theme;
import com.mydiabetesprt.diabetes.partage.view.theme.ThemeUs;

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