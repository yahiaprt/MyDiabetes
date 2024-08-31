package com.yahia.healthysiabires.partage.data.database.ydk;

import android.util.Log;

import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;
import com.j256.ormlite.stmt.QueryBuilder;

import org.joda.time.Interval;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class FoodEatenydk extends Baseydk<FoodEaten> {

    private static final String TAG = FoodEatenydk.class.getSimpleName();

    private static FoodEatenydk instance;

    public static FoodEatenydk getInstance() {
        if (instance == null) {
            instance = new FoodEatenydk();
        }
        return instance;
    }

    private FoodEatenydk() {
        super(FoodEaten.class);
    }

    public List<FoodEaten> getAllOrdered() {
        try {
            // TODO: Distinct
            // TODO: Order by Entre.getDateTime()
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .join(maklaydk.getInstance().getQueryBuilder())
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public long count(Food makla) {
        try {
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .where().eq(FoodEaten.Column.makla, makla)
                    .countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return 0;
        }
    }

    public List<FoodEaten> getAll(Food makla) {
        try {
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .where().eq(FoodEaten.Column.makla, makla)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<FoodEaten> getAll(Meal meal) {
        try {
            return getQueryBuilder()
                    .orderBy(FoodEaten.Column.CREATED_AT, false)
                    .where().eq(FoodEaten.Column.MEAL, meal)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<FoodEaten> getAll(Interval interval) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = EntryDao.getInstance().getQueryBuilder();
            queryBuilderEntry
                    .where().ge(Entry.Column.DATE, interval.getStart())
                    .and().le(Entry.Column.DATE, interval.getEnd());
            QueryBuilder<Meal, Long> queryBuilderMeal = mesorationydk.getInstance(Meal.class).getQueryBuilder();
            return getQueryBuilder().join(queryBuilderMeal.join(queryBuilderEntry)).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
}
