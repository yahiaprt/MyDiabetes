package com.yahia.healthysiabires.partage.data.database.entity;

import com.yahia.healthysiabires.future.export.Backupable;
import com.yahia.healthysiabires.future.export.Exportable;
import com.yahia.healthysiabires.future.export.job.Export;
import com.yahia.healthysiabires.partage.Helper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable
public class Entry extends BaseEntite implements Backupable, Exportable {

    public static final String BACKUP_KEY = "entry";

    public class Column extends BaseEntite.Column {
        public static final String DATE = "date";
        public static final String NOTE = "note";
    }

    @DatabaseField(columnName = Column.DATE)
    private DateTime date;

    @DatabaseField(columnName = Column.NOTE)
    private String note;

    @ForeignCollectionField
    private ForeignCollection<mesoration> measurements;

    private List<mesoration> measurementCache;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ForeignCollection<mesoration> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ForeignCollection<mesoration> measurements) {
        this.measurements = measurements;
    }

    // TODO: Get rid of measurementCache (Currently required to enable lazy loading for generic measurements
    public List<mesoration> getMeasurementCache() {
        if (measurementCache == null) {
            measurementCache = new ArrayList<>();
        }
        return measurementCache;
    }

    public void setMeasurementCache(List<mesoration> measurementCache) {
        this.measurementCache = measurementCache;
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        return new String[]{DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).print(date), note};
    }

    @Override
    public String[] getValuesForExport() {
        return new String[] {
            String.format("%s %s",
                Helper.getDateFormat().print(date),
                Helper.getTimeFormat().print(date)
            ).toLowerCase(),
            note
        };
    }
}
