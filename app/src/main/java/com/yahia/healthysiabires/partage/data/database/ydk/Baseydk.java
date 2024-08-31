package com.yahia.healthysiabires.partage.data.database.ydk;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yahia.healthysiabires.partage.data.database.Database;
import com.yahia.healthysiabires.partage.data.database.entity.BaseEntite;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Faltenreich on 05.09.2015.
 */
public abstract class Baseydk<T extends BaseEntite> {

    private static final String TAG = Baseydk.class.getSimpleName();

    static final long PAGE_SIZE = 50;

    private Class<T> clazz;

    Baseydk(Class<T> clazz) {
        this.clazz = clazz;
    }

    Class<T> getClazz() {
        return clazz;
    }

    @NonNull
    private Dao<T, Long> getdao() {
        try {
            return Database.getInstance().getDatabaseHelper().getDao(clazz);
        } catch (SQLException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    @NonNull
    QueryBuilder<T, Long> getQueryBuilder() {
        return getdao().queryBuilder();
    }

    @NonNull
    DeleteBuilder<T, Long> getDeleteBuilder() {
        return getdao().deleteBuilder();
    }

    @Nullable
    List<String[]> queryRaw(String query) {
        try {
            GenericRawResults<String[]> rawResults = getdao().queryRaw(query);
            return rawResults.getResults();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public T getById(long id) {
        try {
            return getQueryBuilder().where().eq(BaseEntite.Column.ID, id).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public T get(T object) {
        return getById(object.getId());
    }

    public List<T> getAll() {
        try {
            return getQueryBuilder().query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    public long countAll() {
        try {
            return getQueryBuilder().countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return 0;
        }
    }

    public T createOrUpdate(T object) {
        try {
            DateTime now = DateTime.now();
            T existingObject = get(object);
            if (existingObject != null) {
                object.setUpdatedAt(now);
                getdao().update(object);
            } else {
                object.setCreatedAt(now);
                object.setUpdatedAt(now);
                getdao().create(object);
            }
            return object;
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public void bulkCreateOrUpdate(final List<T> objects) {
        if (objects != null && objects.size() > 0) {
            try {
                getdao().callBatchTasks((Callable<Void>) () -> {
                    for (T object : objects) {
                        createOrUpdate(object);
                    }
                    return null;
                });
            } catch (Exception exception) {
                Log.e(TAG, exception.getLocalizedMessage());
            }
        }
    }

    public void deleteAll() {
        try {
            TableUtils.clearTable(Database.getInstance().getDatabaseHelper().getConnectionSource(), clazz);
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    public int delete(List<T> objects) {
        try {
            if (objects != null && objects.size() > 0) {
                return getdao().delete(objects);
            } else {
                return 0;
            }
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return 0;
        }
    }

    public int delete(T object) {
        try {
            if (object != null) {
                return getdao().delete(object);
            } else {
                return 0;
            }
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return 0;
        }
    }
}