package com.mydiabetesprt.diabetes.partage.data.database.entity;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Insulin extends mesoration {

    public class Column extends mesoration.Column {
        public static final String BOLUS = "bolus";
        public static final String CORRECTION = "correction";
        public static final String BASAL = "basal";
    }

    @DatabaseField(columnName = Column.BOLUS)
    private float bolus;

    @DatabaseField(columnName = Column.CORRECTION)
    private float correction;

    @DatabaseField(columnName = Column.BASAL)
    private float basal;

    public float getBolus() {
        return bolus;
    }

    public void setBolus(float bolus) {
        this.bolus = bolus;
    }

    public float getCorrection() {
        return correction;
    }

    public void setCorrection(float correction) {
        this.correction = correction;
    }

    public float getBasal() {
        return basal;
    }

    public void setBasal(float basal) {
        this.basal = basal;
    }

    @Override
    public type getCategory() {
        return type.INSULIN;
    }

    @Override
    public float[] getValues() {
        return new float[] { bolus, correction, basal };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            bolus = values[0];
            if (values.length > 1) {
                correction = values[1];
                if (values.length > 2) {
                    basal = values[2];
                }
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        float total = bolus + correction + basal;
        float customTotal = PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), total);
        return FloatUs.parseFloat(customTotal);
    }

    @Override
    public String print(Context context) {
        float total = bolus + correction + basal;
        StringBuilder stringBuilder = new StringBuilder(PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), total));
        stringBuilder.append(String.format(" %s", PreferenceHelper.getInstance().getUnitAcronym(getCategory())));
        stringBuilder.append(": ");
        boolean isFirstValue = true;
        if (getBolus() != 0) {
            stringBuilder.append(String.format("%s %s",
                PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), bolus),
                context.getString(R.string.bolus)));
            isFirstValue = false;
        }
        if (getCorrection() != 0) {
            if (!isFirstValue) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(String.format("%s %s",
                PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), correction),
                context.getString(R.string.correction)));
            isFirstValue = false;
        }
        if (getBasal() != 0) {
            if (!isFirstValue) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(String.format("%s %s",
                PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), basal),
                context.getString(R.string.basal)));
        }
        stringBuilder.append("");
        return stringBuilder.toString().trim();
    }
}
