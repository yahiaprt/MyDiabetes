package com.yahia.healthysiabires.partage.data.database.entity;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class glycemie extends mesoration {

    public class Column extends mesoration.Column {
        public static final String MGDL = "mgDl";
    }

    @DatabaseField(columnName = Column.MGDL)
    private float mgDl;

    public float getMgDl() {
        return mgDl;
    }

    public void setMgDl(float mgDl) {
        this.mgDl = mgDl;
    }

    @Override
    public type getCategory() {
        return type.BLOODSUGAR;
    }

    @Override
    public float[] getValues() {
        return new float[] { mgDl };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            mgDl = values[0];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), mgDl);
    }
}
