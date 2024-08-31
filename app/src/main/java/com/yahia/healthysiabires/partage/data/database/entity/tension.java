package com.yahia.healthysiabires.partage.data.database.entity;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.healthysiabiresApplication;
import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class tension extends mesoration {

    public class Column extends mesoration.Column {
        public static final String SYSTOLIC = "systolic";
        public static final String DIASTOLIC = "diastolic";
    }

    @DatabaseField(columnName = Column.SYSTOLIC)
    private float systolic;

    @DatabaseField(columnName = Column.DIASTOLIC)
    private float diastolic;

    public float getSystolic() {
        return systolic;
    }

    public void setSystolic(float systolic) {
        this.systolic = systolic;
    }

    public float getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(float diastolic) {
        this.diastolic = diastolic;
    }

    @Override
    public type getCategory() {
        return type.PRESSURE;
    }

    @Override
    public float[] getValues() {
        return new float[] { systolic, diastolic };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 1) {
            systolic = values[0];
            diastolic = values[1];
        }
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(
            getCategory(), systolic) + " " +
            healthysiabiresApplication.getContext().getString(R.string.to) + " " +
            PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), diastolic
        );
    }
}
