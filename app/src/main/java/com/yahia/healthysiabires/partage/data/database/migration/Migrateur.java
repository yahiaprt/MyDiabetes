package com.yahia.healthysiabires.partage.data.database.migration;

import android.content.Context;

import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;

public class Migrateur {

    private static Migrateur instance;

    public static Migrateur getInstance() {
        if (instance == null) {
            instance = new Migrateur();
        }
        return instance;
    }

    private Migrateur() {}

    public void start(Context context) {
        int oldVersionCode = PreferenceHelper.getInstance().getVersionCode();
        if (oldVersionCode == 39) {
            new MigrateSodiumTask(context).execute();
        }
    }
}
