package com.yahia.healthysiabires.partage.data.database.migration;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.yahia.healthysiabires.partage.data.database.ydk.maklaydk;
import com.yahia.healthysiabires.partage.data.database.entity.Food;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Version 3.4.2:
 * Fixes amount of sodium in common makla which was wrongly imported in grams instead of milligrams
 */
public class MigrateSodiumTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = MigrateSodiumTask.class.getSimpleName();

    private WeakReference<Context> context;

    MigrateSodiumTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Food> maklaList = maklaydk.getInstance().getAllCommon(context.get());
        for (Food makla : maklaList) {
            Float sodium = makla.getSodium();
            makla.setSodium(sodium != null && sodium > 0 ? sodium / 1000 : null);
        }
        maklaydk.getInstance().bulkCreateOrUpdate(maklaList);
        Log.i(TAG, String.format("Fixed sodium of %d common food items", maklaList.size()));
        return null;
    }
}