package com.yahia.healthysiabires.partage.data.database.ydk;

import android.util.Log;

import com.yahia.healthysiabires.partage.data.database.entity.Activity;
import com.yahia.healthysiabires.partage.data.database.entity.BaseEntite;
import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.glycemie;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.entity.HbA1c;
import com.yahia.healthysiabires.partage.data.database.entity.Insulin;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;
import com.yahia.healthysiabires.partage.data.database.entity.OxySaturation;
import com.yahia.healthysiabires.partage.data.database.entity.tension;
import com.yahia.healthysiabires.partage.data.database.entity.Pulsation;
import com.yahia.healthysiabires.partage.data.database.entity.poits;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTableConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Faltenreich on 06.09.2015.
 */
public class mesorationydk<M extends mesoration> extends Baseydk<M> {

    private static final String TAG = mesorationydk.class.getSimpleName();

    private static HashMap<Class, mesorationydk> instances;

    public static <M extends mesoration> mesorationydk getInstance(Class<M> clazz) {
        if (instances == null) {
            instances = new HashMap<>();
        }
        mesorationydk mesorationydk = instances.get(clazz);
        if (mesorationydk == null) {
            instances.put(clazz, new mesorationydk<>(clazz));
            mesorationydk = instances.get(clazz);
        }
        return mesorationydk;
    }

    private mesorationydk(Class<M> clazz) {
        super(clazz);
    }

    @Override
    public M createOrUpdate(M object) {
        M entity = super.createOrUpdate(object);
        switch (entity.getCategory()) {
            case MEAL:
                createOrUpdate((Meal) entity);
                break;
        }
        return entity;
    }

    @Override
    public int delete(M object) {
        switch (object.getCategory()) {
            case MEAL:
                Meal meal = (Meal) object;
                for (FoodEaten FoodEaten : meal.getFoodEaten()) {
                    meal.getFoodEatenCache().add(FoodEaten);
                    FoodEatenydk.getInstance().delete(FoodEaten);
                }
                break;
        }
        return super.delete(object);
    }

    private void createOrUpdate(Meal meal) {
        for (FoodEaten FoodEatenOld : FoodEatenydk.getInstance().getAll(meal)) {
            if (!meal.getFoodEatenCache().contains(FoodEatenOld)) {
                FoodEatenydk.getInstance().delete(FoodEatenOld);
            }
        }
        for (FoodEaten FoodEaten : meal.getFoodEatenCache()) {
            FoodEaten.setMeal(meal);
            FoodEatenydk.getInstance().createOrUpdate(FoodEaten);
        }
    }

    public float function(SqlFunction sqlFunction, String column, Interval interval) {
        String classNameEntry = DatabaseTableConfig.extractTableName(null, Entry.class);
        String classNameMeasurement = DatabaseTableConfig.extractTableName(null, getClazz());
        long intervalStart = interval.getStart().withTimeAtStartOfDay().getMillis();
        long intervalEnd = interval.getEnd().withTime(
                DateTimeConstants.HOURS_PER_DAY - 1,
                DateTimeConstants.MINUTES_PER_HOUR - 1,
                DateTimeConstants.SECONDS_PER_MINUTE - 1,
                DateTimeConstants.MILLIS_PER_SECOND - 1)
                .getMillis();
        String query = "SELECT " + sqlFunction.function + "(" + column + ")" +
                " FROM " + classNameMeasurement +
                " INNER JOIN " + classNameEntry +
                " ON " + classNameMeasurement + "." + mesoration.Column.ENTRY +
                " = " + classNameEntry + "." + BaseEntite.Column.ID +
                " AND " + classNameEntry + "." + Entry.Column.DATE +
                " >= " + intervalStart +
                " AND " + classNameEntry + "." + Entry.Column.DATE +
                " <= " + intervalEnd + ";";

        List<String[]> results = queryRaw(query);
        if (results == null) {
            return 0;
        }

        String[] resultArray = results.get(0);
        String result = resultArray[0];
        try {
            return FloatUs.parseNumber(result);
        } catch (NumberFormatException exception) {
            return 0;
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public mesoration getAvgMeasurement(type category, Interval interval) {
        long daysBetween = interval.toDuration().getStandardDays() + 1;
        switch (category) {
            case BLOODSUGAR:
                glycemie bloodSugar = new glycemie();
                bloodSugar.setMgDl(function(SqlFunction.AVG, glycemie.Column.MGDL, interval));
                return bloodSugar;
            case INSULIN:
                Insulin insulin = new Insulin();
                insulin.setBolus(function(SqlFunction.SUM, Insulin.Column.BOLUS, interval) / daysBetween);
                insulin.setBasal(function(SqlFunction.SUM, Insulin.Column.BASAL, interval) / daysBetween);
                insulin.setCorrection(function(SqlFunction.SUM, Insulin.Column.CORRECTION, interval) / daysBetween);
                return insulin;
            case MEAL:
                Meal meal = new Meal();
                float avg = function(SqlFunction.SUM, Meal.Column.CARBOHYDRATES, interval) / daysBetween;
                float FoodEatenSum = 0;
                List<FoodEaten> FoodEatenList = FoodEatenydk.getInstance().getAll(interval);
                for (FoodEaten FoodEaten : FoodEatenList) {
                    FoodEatenSum += FoodEaten.getCarbohydrates();
                }
                avg = avg + (FoodEatenSum / daysBetween);
                meal.setCarbohydrates(avg);
                return meal;
            case ACTIVITY:
                Activity activity = new Activity();
                activity.setMinutes((int) (function(SqlFunction.SUM, Activity.Column.MINUTES, interval) / daysBetween));
                return activity;
            case HBA1C:
                HbA1c hbA1c = new HbA1c();
                hbA1c.setPercent(function(SqlFunction.AVG, HbA1c.Column.PERCENT, interval));
                return hbA1c;
            case WEIGHT:
                poits weight = new poits();
                weight.setKilogram(function(SqlFunction.AVG, poits.Column.KILOGRAM, interval));
                return weight;
            case PULSE:
                Pulsation pulse = new Pulsation();
                pulse.setFrequency(function(SqlFunction.AVG, Pulsation.Column.FREQUENCY, interval));
                return pulse;
            case PRESSURE:
                tension pressure = new tension();
                pressure.setSystolic(function(SqlFunction.AVG, tension.Column.SYSTOLIC, interval));
                pressure.setDiastolic(function(SqlFunction.AVG, tension.Column.DIASTOLIC, interval));
                return pressure;
            case OXYGEN_SATURATION:
                OxySaturation oxygenSaturation = new OxySaturation();
                oxygenSaturation.setPercent(function(SqlFunction.AVG, OxySaturation.Column.PERCENT, interval));
                return oxygenSaturation;
            default:
                return null;
        }
    }

    public M getMeasurement(Entry entry) {
        try {
            return getQueryBuilder().where().eq(mesoration.Column.ENTRY, entry).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not fetch measurement of types '%s'", getClazz().toString()));
            return null;
        }
    }

    public List<M> getMeasurements(DateTime day) {
        try {
            if (day == null) {
                return new ArrayList<>();
            }
            DateTime start = day.withTimeAtStartOfDay();
            DateTime end = day.withTime(DateTimeConstants.HOURS_PER_DAY - 1,
                    DateTimeConstants.MINUTES_PER_HOUR - 1,
                    DateTimeConstants.SECONDS_PER_MINUTE - 1,
                    DateTimeConstants.MILLIS_PER_SECOND - 1);
            QueryBuilder<M, Long> measurementQb = getQueryBuilder();
            QueryBuilder<Entry, Long> entryQb = EntryDao.getInstance().getQueryBuilder();
            entryQb
                    .orderBy(Entry.Column.DATE, true)
                    .where().gt(Entry.Column.DATE, start).and().lt(Entry.Column.DATE, end);
            return measurementQb.join(entryQb).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    public int deleteMeasurements(Entry entry) {
        try {
            DeleteBuilder deleteBuilder = getDeleteBuilder();
            deleteBuilder.where().eq(mesoration.Column.ENTRY, entry);
            return deleteBuilder.delete();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not delete '%s' measurements of entry with id %d", getClazz().toString(), entry.getId()));
            return 0;
        }
    }
}
