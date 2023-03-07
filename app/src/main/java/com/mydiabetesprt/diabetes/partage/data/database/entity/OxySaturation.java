package com.mydiabetesprt.diabetes.partage.data.database.entity;

import androidx.annotation.NonNull;

import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class OxySaturation extends mesoration {

    public class Column extends mesoration.Column {
        public static final String PERCENT = "percent";
    }

    @DatabaseField(columnName = Column.PERCENT)
    private float percent;

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public type getCategory() {
        return type.OXYGEN_SATURATION;
    }

    @Override
    public float[] getValues() {
        return new float[] { percent };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            percent = values[0];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), percent);
    }
}
