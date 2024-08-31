package com.yahia.healthysiabires.partage.data.database.entity;

import android.content.Context;

import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.future.export.Backupable;
import com.yahia.healthysiabires.future.export.Exportable;
import com.j256.ormlite.field.DatabaseField;

import org.apache.commons.lang3.ArrayUtils;

public abstract class mesoration extends BaseEntite implements Backupable, Exportable {

    public static final String BACKUP_KEY = "measurement";

    public class Column extends BaseEntite.Column {
        public static final String ENTRY = "entry";
    }

    @DatabaseField(columnName = Column.ENTRY, foreign = true, foreignAutoRefresh = true)
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public abstract type getCategory();

    public String[] getValuesForUI() {
        float[] values = getValues();
        String[] valuesForUI = new String[values.length];
        for (int position = 0; position < values.length; position++) {
            float value = values[position];
            if (value != 0) {
                float valueFormatted = PreferenceHelper.getInstance().formatDefaultToCustomUnit(getCategory(), value);
                valuesForUI[position] = FloatUs.parseFloat(valueFormatted);
            }
        }
        return valuesForUI;
    }

    public abstract float[] getValues();

    public abstract void setValues(float... values);

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        float[] values = getValues();
        String[] valuesForBackup = new String[values.length];
        for (int index = 0; index < values.length; index++) {
            valuesForBackup[index] = Float.toString(values[index]);
        }
        return ArrayUtils.addAll(new String[]{getCategory().name().toLowerCase()}, valuesForBackup);
    }

    @Override
    public String[] getValuesForExport() {
        return ArrayUtils.addAll(new String[]{getCategory().name().toLowerCase()}, getValuesForUI());
    }

    public String print(Context context) {
        return String.format("%s %s",
            toString(),
            PreferenceHelper.getInstance().getUnitAcronym(getCategory())
        );
    }
}
