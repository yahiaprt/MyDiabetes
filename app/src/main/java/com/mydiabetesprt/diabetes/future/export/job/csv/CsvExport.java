package com.mydiabetesprt.diabetes.future.export.job.csv;

import android.os.AsyncTask;
import android.util.Log;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.DatabaseHelper;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryTagDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.maklaydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.Tagydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.EntryTag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.database.entity.mesoration;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Tag;
import com.mydiabetesprt.diabetes.future.export.job.Export;
import com.mydiabetesprt.diabetes.future.export.job.ExportCallback;
import com.mydiabetesprt.diabetes.future.export.job.FileType;
import com.opencsv.CSVWriter;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExport extends AsyncTask<Void, String, File> {

    private static final String TAG = CsvExport.class.getSimpleName();

    private CsvExportConfig config;

    public CsvExport(CsvExportConfig config) {
        this.config = config;
    }

    private ExportCallback getCallback() {
        return config.getCallback();
    }

    @Override
    protected File doInBackground(Void... params) {
        DateTime dateStart = config.getDateStart();
        DateTime dateEnd = config.getDateEnd();
        type[] categories = config.getCategories();
        boolean isBackup = config.isBackup();

        File file = isBackup?
                Export.getBackupFile(config, FileType.CSV) :
                Export.getExportFile(config);
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter, CsvMeta.CSV_DELIMITER);

            if (isBackup) {
                // Meta information to detect the data scheme in future iterations
                String[] meta = new String[]{
                    CsvMeta.CSV_KEY_META,
                        Integer.toString(DatabaseHelper.getVersion())};
                writer.writeNext(meta);

                List<Tag> tags = Tagydk.getInstance().getAll();
                for (Tag tag : tags) {
                    writer.writeNext(ArrayUtils.add(tag.getValuesForBackup(), 0, tag.getKeyForBackup()));
                }

                List<Food> maklas = maklaydk.getInstance().getAllFromUser();
                for (Food makla: maklas) {
                    writer.writeNext(ArrayUtils.add(makla.getValuesForBackup(), 0, makla.getKeyForBackup()));
                }
            }

            List<Entry> entries = dateStart != null && dateEnd != null ?
                    EntryDao.getInstance().getEntriesBetween(dateStart, dateEnd) :
                    EntryDao.getInstance().getAll();
            int position = 0;
            for (Entry entry : entries) {
                publishProgress(String.format("%s %d/%d",
                        config.getContext().getString(R.string.entry),
                        position,
                        entries.size()));

                writer.writeNext(isBackup ? ArrayUtils.add(entry.getValuesForBackup(), 0, entry.getKeyForBackup()) : entry.getValuesForExport());

                List<mesoration> measurements = categories != null ? EntryDao.getInstance().getMeasurements(entry, categories) : EntryDao.getInstance().getMeasurements(entry);
                for (mesoration measurement : measurements) {
                    writer.writeNext(isBackup ? ArrayUtils.add(measurement.getValuesForBackup(), 0, measurement.getKeyForBackup()) : measurement.getValuesForExport());

                    if (isBackup && measurement instanceof Meal) {
                        Meal meal = (Meal) measurement;
                        for (FoodEaten FoodEaten : meal.getFoodEaten()) {
                            writer.writeNext(ArrayUtils.add(FoodEaten.getValuesForBackup(), 0, FoodEaten.getKeyForBackup()));
                        }
                    }
                }

                if (isBackup) {
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    for (EntryTag entryTag : entryTags) {
                        writer.writeNext(ArrayUtils.add(entryTag.getValuesForBackup(), 0, entryTag.getKeyForBackup()));
                    }
                }

                position++;
            }

            writer.close();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }

        return file;
    }

    @Override
    protected void onProgressUpdate(String... message) {
        if (getCallback() != null) {
            getCallback().onProgress(message[0]);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (getCallback() != null) {
            if (file != null) {
                getCallback().onSuccess(file, FileType.CSV.mimeType);
            } else {
                getCallback().onError();
            }
        }
    }
}