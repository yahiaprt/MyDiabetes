package com.yahia.healthysiabires.partage.data.database.ydk;

import android.util.Log;

import androidx.annotation.Nullable;

import com.yahia.healthysiabires.partage.data.database.entity.BaseEntite;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tagydk extends Baseydk<Tag> {

    private static final String TAG = Tagydk.class.getSimpleName();

    private static Tagydk instance;

    public static Tagydk getInstance() {
        if (instance == null) {
            instance = new Tagydk();
        }
        return instance;
    }

    private Tagydk() {
        super(Tag.class);
    }

    @Override
    public List<Tag> getAll() {
        try {
            return getQueryBuilder().orderBy(BaseEntite.Column.UPDATED_AT, false).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Tag> getRecent() {
        try {
            return getQueryBuilder().orderBy(BaseEntite.Column.UPDATED_AT, false).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    @Nullable
    public Tag getByName(String name) {
        try {
            return getQueryBuilder().where().eq(Tag.Column.NAME, name).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }
}
