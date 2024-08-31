package com.yahia.healthysiabires.partage.data.database.entity;

import com.yahia.healthysiabires.future.export.Backupable;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class EntryTag extends BaseEntite implements Backupable {

    public static final String BACKUP_KEY = "entryTag";

    public class Column extends BaseEntite.Column {
        public static final String ENTRY = "entry";
        public static final String TAG = "tag";
    }

    @DatabaseField(columnName = Column.ENTRY, foreign = true, foreignAutoRefresh = true)
    private Entry entry;

    @DatabaseField(columnName = Column.TAG, foreign = true, foreignAutoRefresh = true)
    private Tag tag;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        return new String[]{tag.getName()};
    }
}
