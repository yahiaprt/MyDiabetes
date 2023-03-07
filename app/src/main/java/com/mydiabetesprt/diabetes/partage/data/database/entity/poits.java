package com.mydiabetesprt.diabetes.partage.data.database.entity;

import androidx.annotation.NonNull;

import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class poits extends mesoration {

    public class Column extends mesoration.Column {
        public static final String KILOGRAM = "kilogram";
    }

    @DatabaseField(columnName = Column.KILOGRAM)
    private float kilogram;

    public float getKilogram() {
        return kilogram;
    }

    public void setKilogram(float kilogram) {
        this.kilogram = kilogram;
    }

    @Override
    public type getCategory() {
        return type.WEIGHT;
    }

    @Override
    public float[] getValues() {
        return new float[] { kilogram };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            kilogram = values[0];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), kilogram);
    }
}
