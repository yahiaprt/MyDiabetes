package com.mydiabetesprt.diabetes.partage.data.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.maklaydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.mesorationydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.Tagydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.glycemie;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Tag;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.future.export.job.csv.CsvMeta;
import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.opencsv.CSVReader;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ImportanceHelper {

    private static final String TAG = ImportanceHelper.class.getSimpleName();
    private static final String makla_CSV_FILE_NAME = "food_common.csv";
    private static final String TAGS_CSV_FILE_NAME = "tags.csv";

    public static void createTestData() {
        new CreateTestData().execute();
    }

    public static void validateImports(Context context) {
        Locale locale = Helper.getLocale();
        validateTagImport(context, locale);
        validatemaklaImport(context, locale);
    }

    private static void validatemaklaImport(Context context, Locale locale) {
        if (!PreferenceHelper.getInstance().didImportCommonmakla(locale)) {
            new ImportmaklaTask(context, locale).execute();
        }
    }

    private static void validateTagImport(Context context, Locale locale) {
        if (!PreferenceHelper.getInstance().didImportTags(locale)) {
            new ImportTagsTask(context, locale).execute();
        }
    }

    private static int getLanguageColumn(String languageCode, String[] row) {
        int languageColumn = 0;
        for (int column = 0; column < 4; column++) {
            String availableLanguageCode = row[column];
            if (languageCode.startsWith(availableLanguageCode.substring(0, 1))) {
                languageColumn = column;
                break;
            }
        }
        return languageColumn;
    }

    private static CSVReader getCsvReader(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return new CSVReader(bufferedReader, CsvMeta.CSV_DELIMITER);
    }

    private static class ImportmaklaTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> context;
        private Locale locale;

        ImportmaklaTask(Context context, Locale locale) {
            this.context = new WeakReference<>(context);
            this.locale = locale;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CSVReader reader = getCsvReader(context.get(), makla_CSV_FILE_NAME);

                String languageCode = locale.getLanguage();
                String[] nextLine = reader.readNext();
                int languageRow = getLanguageColumn(languageCode, nextLine);

                List<Food> maklaList = new ArrayList<>();
                while ((nextLine = reader.readNext()) != null) {

                    if (nextLine.length >= 13) {
                        Food makla = new Food();

                        makla.setName(nextLine[languageRow]);
                        makla.setIngredients(makla.getName());
                        makla.setLabels(context.get().getString(R.string.makla_common));
                        makla.setLanguageCode(languageCode);

                        // Main nutritions are given in grams, so we take them as they are
                        makla.setCarbohydrates(FloatUs.parseNullableNumber(nextLine[4]));
                        makla.setEnergy(FloatUs.parseNullableNumber(nextLine[5]));
                        makla.setFat(FloatUs.parseNullableNumber(nextLine[6]));
                        makla.setFatSaturated(FloatUs.parseNullableNumber(nextLine[7]));
                        makla.setFiber(FloatUs.parseNullableNumber(nextLine[8]));
                        makla.setProteins(FloatUs.parseNullableNumber(nextLine[9]));
                        makla.setSalt(FloatUs.parseNullableNumber(nextLine[10]));
                        makla.setSugar(FloatUs.parseNullableNumber(nextLine[12]));

                        // Mineral nutritions are given in milligrams, so we divide them by 1.000
                        Float sodium = FloatUs.parseNullableNumber(nextLine[11]);
                        sodium = sodium != null ? sodium / 1000 : null;
                        makla.setSodium(sodium);

                        maklaList.add(makla);
                    }
                }

                Collections.reverse(maklaList);
                maklaydk.getInstance().deleteAll();
                maklaydk.getInstance().bulkCreateOrUpdate(maklaList);

                Log.i(TAG, String.format("Imported %d common food items from csv", maklaList.size()));

            } catch (IOException exception) {
                Log.e(TAG, exception.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PreferenceHelper.getInstance().setDidImportCommonmakla(locale, true);
        }
    }

    private static class ImportTagsTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> context;
        private Locale locale;

        ImportTagsTask(Context context, Locale locale) {
            this.context = new WeakReference<>(context);
            this.locale = locale;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CSVReader reader = getCsvReader(context.get(), TAGS_CSV_FILE_NAME);

                String languageCode = locale.getLanguage();
                String[] nextLine = reader.readNext();
                int languageRow = getLanguageColumn(languageCode, nextLine);

                List<Tag> tags = new ArrayList<>();
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length >= 4) {
                        Tag tag = new Tag();
                        tag.setName(nextLine[languageRow]);
                        tags.add(tag);
                    }
                }

                Tagydk.getInstance().deleteAll();
                Collections.reverse(tags);
                Tagydk.getInstance().bulkCreateOrUpdate(tags);

                Log.i(TAG, String.format("Imported %d tags from csv", tags.size()));

            } catch (IOException exception) {
                Log.e(TAG, exception.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PreferenceHelper.getInstance().setDidImportTags(locale, true);
        }
    }

    private static class CreateTestData extends AsyncTask<Void, Void, Void> {

        private static final int DATA_COUNT = 1000;

        @Override
        protected Void doInBackground(Void... params) {
            for (int count = 0; count < DATA_COUNT; count++) {
                DateTime dateTime = DateTime.now().minusDays(count);
                Entry entry = new Entry();
                entry.setDate(dateTime);
                entry.setNote("Test");
                EntryDao.getInstance().createOrUpdate(entry);

                glycemie bloodSugar = new glycemie();
                bloodSugar.setMgDl(100);
                bloodSugar.setEntry(entry);
                mesorationydk.getInstance(glycemie.class).createOrUpdate(bloodSugar);

                Meal meal = new Meal();
                meal.setCarbohydrates(20);
                meal.setEntry(entry);
                mesorationydk.getInstance(Meal.class).createOrUpdate(meal);

                Log.d(TAG, "Created test data: " + (count + 1) + "/" + DATA_COUNT);
            }
            return null;
        }
    }
}
