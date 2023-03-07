package com.mydiabetesprt.diabetes.future.export.job.csv;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.mydiabetesprt.diabetes.partage.data.database.DatabaseHelper;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.data.database.entity.mesoration;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryTagDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.maklaydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.FoodEatenydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.mesorationydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.Tagydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.EntryTag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Tag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.deprecated.typeDeprecated;
import com.mydiabetesprt.diabetes.future.export.job.Export;
import com.mydiabetesprt.diabetes.future.export.job.ExportCallback;
import com.mydiabetesprt.diabetes.future.export.job.FileType;
import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.opencsv.CSVReader;

import org.joda.time.format.DateTimeFormat;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CsvImport extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = CsvImport.class.getSimpleName();

    private WeakReference<Context> context;
    private Uri uri;
    private ExportCallback callback;

    public CsvImport(Context context, Uri uri) {
        this.context = new WeakReference<>(context);
        this.uri = uri;
    }

    public void setCallback(ExportCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            InputStream inputStream = context.get().getContentResolver().openInputStream(uri);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream), CsvMeta.CSV_DELIMITER);
            String[] nextLine = reader.readNext();

            // First version was without meta information
            if (!nextLine[0].equals(CsvMeta.CSV_KEY_META)) {
                importFromVersion1_0(reader, nextLine);
            } else {
                int databaseVersion = Integer.parseInt(nextLine[1]);
                if (databaseVersion == DatabaseHelper.DATABASE_VERSION_1_1) {
                    importFromVersion1_1(reader, nextLine);
                } else if (databaseVersion <= DatabaseHelper.DATABASE_VERSION_2_2) {
                    importFromVersion2_2(reader, nextLine);
                } else {
                    importFromVersion3_0(reader, nextLine);
                }
            }
            reader.close();
            return true;

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
            return false;
        }
    }

    private void importFromVersion1_0(CSVReader reader, String[] nextLine) throws Exception {
        while (nextLine != null) {
            Entry entry = new Entry();
            entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
            String note = nextLine[2];
            entry.setNote(note != null && note.length() > 0 ? note : null);
            EntryDao.getInstance().createOrUpdate(entry);
            try {
                typeDeprecated categoryDeprecated = Helper.valueOf(typeDeprecated.class, nextLine[2]);
                type category = categoryDeprecated.toUpdate();
                mesoration measurement = category.toClass().newInstance();
                measurement.setValues(FloatUs.parseNumber(nextLine[0]));
                measurement.setEntry(entry);
                mesorationydk.getInstance(category.toClass()).createOrUpdate(measurement);
            } catch (InstantiationException e) {
                Log.e(TAG, e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e(TAG, e.getMessage());
            }
            nextLine = reader.readNext();
        }
    }

    private void importFromVersion1_1(CSVReader reader, String[] nextLine) throws Exception {
        Entry entry = null;
        while ((nextLine = reader.readNext()) != null) {
            String key = nextLine[0];
            if (key.equalsIgnoreCase(Entry.BACKUP_KEY)) {
                entry = new Entry();
                entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                String note = nextLine[2];
                entry.setNote(note != null && note.length() > 0 ? note : null);
                entry = EntryDao.getInstance().createOrUpdate(entry);
            } else if (key.equalsIgnoreCase(mesoration.BACKUP_KEY) && entry != null) {
                try {
                    typeDeprecated categoryDeprecated = Helper.valueOf(typeDeprecated.class, nextLine[2]);
                    type category = categoryDeprecated.toUpdate();
                    mesoration measurement = category.toClass().newInstance();
                    measurement.setValues(new float[]{FloatUs.parseNumber(nextLine[1])});
                    measurement.setEntry(entry);
                    mesorationydk.getInstance(category.toClass()).createOrUpdate(measurement);
                } catch (InstantiationException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    private void importFromVersion2_2(CSVReader reader, String[] nextLine) throws Exception {
        Entry entry = null;
        while ((nextLine = reader.readNext()) != null) {
            String key = nextLine[0];
            if (key.equalsIgnoreCase(Entry.BACKUP_KEY)) {
                entry = new Entry();
                entry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                String note = nextLine[2];
                entry.setNote(note != null && note.length() > 0 ? note : null);
                entry = EntryDao.getInstance().createOrUpdate(entry);
            } else if (key.equalsIgnoreCase(mesoration.BACKUP_KEY) && entry != null) {
                try {
                    type category = Helper.valueOf(type.class, nextLine[1]);
                    mesoration measurement = category.toClass().newInstance();

                    List<Float> valueList = new ArrayList<>();
                    for (int position = 2; position < nextLine.length; position++) {
                        String valueString = nextLine[position];
                        try {
                            valueList.add(FloatUs.parseNumber(valueString));
                        } catch (NumberFormatException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    float[] values = new float[valueList.size()];
                    for (int position = 0; position < valueList.size(); position++) {
                        values[position] = valueList.get(position);
                    }
                    measurement.setValues(values);
                    measurement.setEntry(entry);
                    mesorationydk.getInstance(category.toClass()).createOrUpdate(measurement);
                } catch (InstantiationException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    private void importFromVersion3_0(CSVReader reader, String[] nextLine) throws Exception {
        Entry lastEntry = null;
        Meal lastMeal = null;
        while ((nextLine = reader.readNext()) != null) {
            switch (nextLine[0]) {
                case Tag.BACKUP_KEY:
                    if (nextLine.length >= 2) {
                        String tagName = nextLine[1];
                        if (Tagydk.getInstance().getByName(tagName) == null) {
                            Tag tag = new Tag();
                            tag.setName(nextLine[1]);
                            Tagydk.getInstance().createOrUpdate(tag);
                        }
                    }
                    break;
                case Food.BACKUP_KEY:
                    if (nextLine.length >= 5) {
                        String maklaName = nextLine[1];
                        if (maklaydk.getInstance().get(maklaName) == null) {
                            Food makla = new Food();
                            makla.setName(maklaName);
                            makla.setBrand(nextLine[2]);
                            makla.setIngredients(nextLine[3]);
                            makla.setCarbohydrates(FloatUs.parseNumber(nextLine[4]));
                            maklaydk.getInstance().createOrUpdate(makla);
                        }
                    }
                    break;
                case Entry.BACKUP_KEY:
                    lastMeal = null;
                    if (nextLine.length >= 3) {
                        lastEntry = new Entry();
                        lastEntry.setDate(DateTimeFormat.forPattern(Export.BACKUP_DATE_FORMAT).parseDateTime(nextLine[1]));
                        String note = nextLine[2];
                        lastEntry.setNote(note != null && note.length() > 0 ? note : null);
                        lastEntry = EntryDao.getInstance().createOrUpdate(lastEntry);
                        break;
                    }
                case mesoration.BACKUP_KEY:
                    if (lastEntry != null && nextLine.length >= 3) {
                        type category = Helper.valueOf(type.class, nextLine[1]);
                        if (category != null) {
                            try {
                                mesoration measurement = category.toClass().newInstance();

                                List<Float> valueList = new ArrayList<>();
                                for (int position = 2; position < nextLine.length; position++) {
                                    String valueString = nextLine[position];
                                    try {
                                        valueList.add(FloatUs.parseNumber(valueString));
                                    } catch (NumberFormatException e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                }
                                float[] values = new float[valueList.size()];
                                for (int position = 0; position < valueList.size(); position++) {
                                    values[position] = valueList.get(position);
                                }
                                measurement.setValues(values);
                                measurement.setEntry(lastEntry);
                                mesorationydk.getInstance(category.toClass()).createOrUpdate(measurement);

                                if (measurement instanceof Meal) {
                                    lastMeal = (Meal) measurement;
                                }
                            } catch (InstantiationException e) {
                                Log.e(TAG, e.getMessage());
                            } catch (IllegalAccessException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                    break;
                case EntryTag.BACKUP_KEY:
                    if (lastEntry != null && nextLine.length >= 2) {
                        Tag tag = Tagydk.getInstance().getByName(nextLine[1]);
                        if (tag != null) {
                            EntryTag entryTag = new EntryTag();
                            entryTag.setEntry(lastEntry);
                            entryTag.setTag(tag);
                            EntryTagDao.getInstance().createOrUpdate(entryTag);
                        }
                    }
                    break;
                case FoodEaten.BACKUP_KEY:
                    if (lastMeal != null && nextLine.length >= 3) {
                        Food makla = maklaydk.getInstance().get(nextLine[1]);
                        if (makla != null) {
                            FoodEaten FoodEaten = new FoodEaten();
                            FoodEaten.setMeal(lastMeal);
                            FoodEaten.setmakla(makla);
                            FoodEaten.setAmountInGrams(FloatUs.parseNumber(nextLine[2]));
                            FoodEatenydk.getInstance().createOrUpdate(FoodEaten);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (callback != null) {
            if (success) {
                callback.onSuccess(null, FileType.CSV.mimeType);
            } else {
                callback.onError();
            }
        }
    }
}
