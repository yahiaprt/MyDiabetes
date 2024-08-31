package com.yahia.healthysiabires.partage.data.database.ydk;

import android.util.Log;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.entity.glycemie;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.entity.Insulin;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.tension;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.future.timeline.jour.tablo.CategoryListItemUtils;
import com.yahia.healthysiabires.partage.data.premier.ArrayUs;
import com.yahia.healthysiabires.future.types.typeComparatorFactory;
import com.yahia.healthysiabires.future.timeline.jour.tablo.CategoryValueListItem;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Faltenreich on 05.09.2015.
 */
public class EntryDao extends Baseydk<Entry> {

    private static final String TAG = EntryDao.class.getSimpleName();

    private static EntryDao instance;

    public static EntryDao getInstance() {
        if (instance == null) {
            instance = new EntryDao();
        }
        return instance;
    }

    private EntryDao() {
        super(Entry.class);
    }

    @Override
    public int delete(Entry entry) {
        for (mesoration measurement : getMeasurements(entry)) {
            entry.getMeasurementCache().add(measurement);
            mesorationydk.getInstance(measurement.getClass()).delete(measurement);
        }
        EntryTagDao.getInstance().delete(EntryTagDao.getInstance().getAll(entry));
        return super.delete(entry);
    }

    public List<Entry> getEntriesOfDay(DateTime day) {
        return getEntriesBetween(day, day);
    }

    public List<Entry> getEntriesBetween(DateTime start, DateTime end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        start = start.withTimeAtStartOfDay();
        end = end.withTime(DateTimeConstants.HOURS_PER_DAY - 1,
            DateTimeConstants.MINUTES_PER_HOUR - 1,
            DateTimeConstants.SECONDS_PER_MINUTE - 1,
            DateTimeConstants.MILLIS_PER_SECOND - 1);
        try {
            return getQueryBuilder()
                .orderBy(Entry.Column.DATE, true)
                .where().gt(Entry.Column.DATE, start)
                .and().lt(Entry.Column.DATE, end)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    public List<mesoration> getMeasurements(Entry entry) {
        return getMeasurements(entry, PreferenceHelper.getInstance().getActiveCategories());
    }

    public List<mesoration> getMeasurements(Entry entry, type[] categories) {
        List<mesoration> measurements = new ArrayList<>();
        for (type category : categories) {
            mesoration measurement = mesorationydk.getInstance(category.toClass()).getMeasurement(entry);
            if (measurement != null) {
                measurements.add(measurement);
            }
        }
        Collections.sort(measurements, typeComparatorFactory.getInstance().createComparatorFromMeasurements());
        return measurements;
    }

    private <M extends mesoration> QueryBuilder<Entry, Long> join(Class<M> clazz) {
        QueryBuilder<Entry, Long> qbOne = getQueryBuilder();
        QueryBuilder<M, Long> qbTwo = mesorationydk.getInstance(clazz).getQueryBuilder();
        try {
            return qbOne.join(qbTwo);
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public <M extends mesoration> Entry getLatestWithMeasurement(Class<M> clazz) {
        try {
            return join(clazz).orderBy(Entry.Column.DATE, false).where().le(Entry.Column.DATE, DateTime.now()).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public <M extends mesoration> List<Entry> getAllWithMeasurementFromToday(Class<M> clazz) {
        try {
            return join(clazz).where().gt(Entry.Column.DATE, DateTime.now().withTimeAtStartOfDay()).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * @return HashMap with non-null but zeroed and default values for given categories and time periods
     */
    public LinkedHashMap<type, CategoryValueListItem[]> getAverageDataTable(DateTime day, type[] categories, int hoursToSkip) {
        if (day == null) {
            return new LinkedHashMap<>();
        }

        int indices = DateTimeConstants.HOURS_PER_DAY / hoursToSkip;

        // Key: type, Value: Fixed-size array of values per hour-index
        LinkedHashMap<type, CategoryValueListItem[]> values = new LinkedHashMap<>();
        for (type category : categories) {
            values.put(category, new CategoryValueListItem[indices]);
        }

        for (type category : categories) {
            // Key: Hour-index, Value: Values of hour-index
            LinkedHashMap<Integer, List<CategoryValueListItem>> valuesOfHours = new LinkedHashMap<>();
            for (int index = 0; index < indices; index++) {
                valuesOfHours.put(index, new ArrayList<>());
            }

            List<mesoration> measurements = mesorationydk.getInstance(category.toClass()).getMeasurements(day);
            for (mesoration measurement : measurements) {
                int index = measurement.getEntry().getDate().hourOfDay().get() / hoursToSkip;
                CategoryValueListItem item = new CategoryValueListItem(category);

                switch (category) {
                    case INSULIN:
                        Insulin insulin = (Insulin) measurement;
                        item.setValueOne(insulin.getBolus());
                        item.setValueTwo(insulin.getCorrection());
                        item.setValueThree(insulin.getBasal());
                        break;
                    case PRESSURE:
                        tension pressure = (tension) measurement;
                        item.setValueOne(pressure.getSystolic());
                        item.setValueTwo(pressure.getDiastolic());
                        break;
                    default:
                        float value = category.stackValues() ? ArrayUs.sum(measurement.getValues()) : ArrayUs.avg(measurement.getValues());
                        if (category == type.MEAL) {
                            for (FoodEaten FoodEaten : ((Meal) measurement).getFoodEaten()) {
                                value += FoodEaten.getCarbohydrates();
                            }
                        }
                        item.setValueOne(value);
                        break;
                }

                List<CategoryValueListItem> valuesOfHour = valuesOfHours.get(index);
                if (valuesOfHour == null) {
                    valuesOfHours.put(index, new ArrayList<>());
                }
                valuesOfHours.get(index).add(item);
            }

            // Average for old values
            for (int index = 0; index < indices; index++) {
                List<CategoryValueListItem> valuesOfHour = valuesOfHours.get(index);
                CategoryValueListItem value = category.stackValues() ?
                    CategoryListItemUtils.sum(category, valuesOfHour) :
                    CategoryListItemUtils.avg(category, valuesOfHour);
                values.get(category)[index] = value;
            }
        }
        return values;
    }

    public long count(type category, DateTime start, DateTime end) {
        try {
            QueryBuilder<Entry, Long> queryBuilder = join(category.toClass());
            if (queryBuilder != null) {
                return queryBuilder
                    .where().ge(Entry.Column.DATE, start)
                    .and().le(Entry.Column.DATE, end)
                    .countOf();
            } else {
                return -1;
            }
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    public long countBelow(DateTime start, DateTime end, float maximumValue) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = getQueryBuilder();
            queryBuilderEntry
                .where().ge(Entry.Column.DATE, start)
                .and().le(Entry.Column.DATE, end);

            QueryBuilder<mesoration, Long> queryBuilderMeasurement = mesorationydk.getInstance(glycemie.class).getQueryBuilder();
            queryBuilderMeasurement.where().lt(glycemie.Column.MGDL, maximumValue);

            return queryBuilderEntry.join(queryBuilderMeasurement).countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    public long countAbove(DateTime start, DateTime end, float minimumValue) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = getQueryBuilder();
            queryBuilderEntry
                .where().ge(Entry.Column.DATE, start)
                .and().le(Entry.Column.DATE, end);

            QueryBuilder<mesoration, Long> queryBuilderMeasurement = mesorationydk.getInstance(glycemie.class).getQueryBuilder();
            queryBuilderMeasurement.where().gt(glycemie.Column.MGDL, minimumValue);

            return queryBuilderEntry.join(queryBuilderMeasurement).countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    public long countBetween(DateTime start, DateTime end, float minimumValue, float maximumValue) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = getQueryBuilder();
            queryBuilderEntry
                .where().ge(Entry.Column.DATE, start)
                .and().le(Entry.Column.DATE, end);

            QueryBuilder<mesoration, Long> queryBuilderMeasurement = mesorationydk.getInstance(glycemie.class).getQueryBuilder();
            queryBuilderMeasurement
                .where().ge(glycemie.Column.MGDL, minimumValue)
                .and().le(glycemie.Column.MGDL, maximumValue);

            return queryBuilderEntry.join(queryBuilderMeasurement).countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    @NonNull
    public List<Entry> search(@NonNull String query, int page, int pageSize) {
        try {
            query = "%" + query + "%";
            QueryBuilder<Tag, Long> tagQb = Tagydk.getInstance().getQueryBuilder();
            tagQb.where().like(Tag.Column.NAME, new SelectArg(query));
            QueryBuilder<EntryTag, Long> entryTagQb = EntryTagDao.getInstance().getQueryBuilder().leftJoinOr(tagQb);
            QueryBuilder<Entry, Long> entryQb = getQueryBuilder().leftJoinOr(entryTagQb)
                .offset((long) (page * pageSize))
                .limit((long) pageSize)
                .orderBy(Entry.Column.DATE, false);
            entryQb.where().like(Entry.Column.NOTE, new SelectArg(query));
            return entryQb.distinct().query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }
}