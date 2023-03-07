package com.mydiabetesprt.diabetes.partage.data.database.entity;

import androidx.annotation.NonNull;

import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Activity extends mesoration {

    @SuppressWarnings("WeakerAccess")
    public class Column extends mesoration.Column {
        public static final String MINUTES = "minutes";
        public static final String TYPE = "type";
    }

    public enum Type {
        // TODO: Integrate in future version
    }

    @DatabaseField(columnName = Column.MINUTES)
    private int minutes;

    @DatabaseField(columnName = Column.TYPE)
    private Type type;

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public com.mydiabetesprt.diabetes.partage.data.database.entity.type getCategory() {
        return com.mydiabetesprt.diabetes.partage.data.database.entity.type.ACTIVITY;
    }

    @Override
    public float[] getValues() {
        return new float[] { minutes };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            minutes = (int) values[0];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), minutes);
    }
}
