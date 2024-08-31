package com.yahia.healthysiabires.partage.data.database.ydk;

import android.util.Log;

import com.yahia.healthysiabires.partage.data.database.entity.BaseServerEntite;

import org.joda.time.DateTime;

import java.sql.SQLException;

/**
 * Created by Faltenreich on 25.09.2016.
 */

class BaseServerydk<T extends BaseServerEntite> extends Baseydk<T> {

    private static final String TAG = BaseServerydk.class.getSimpleName();

    BaseServerydk(Class<T> clazz) {
        super(clazz);
    }

    T getByServerId(String serverId) {
        try {
            return getQueryBuilder().where().eq(BaseServerEntite.Column.SERVER_ID, serverId).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    @Override
    public T get(T object) {
        return object.getServerId() != null ? getByServerId(object.getServerId()) : super.get(object);
    }

    public void softDelete(T entity) {
        entity.setDeletedAt(DateTime.now());
        createOrUpdate(entity);
    }
}
