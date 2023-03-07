package com.mydiabetesprt.diabetes.partage.data.database.ydk;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.BaseServerEntite;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.future.makla.networkage.ydk.Productydk;
import com.mydiabetesprt.diabetes.future.makla.networkage.ydk.SearchResponseydk;
import com.mydiabetesprt.diabetes.future.datetemps.choisirletimeUS;
import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.data.premier.StringUs;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class maklaydk extends BaseServerydk<Food> {

    private static final String TAG = maklaydk.class.getSimpleName();

    private static maklaydk instance;

    public static maklaydk getInstance() {
        if (instance == null) {
            instance = new maklaydk();
        }
        return instance;
    }

    private maklaydk() {
        super(Food.class);
    }

    @Override
    public List<Food> getAll() {
        try {
            return getQueryBuilder()
                    .orderBy(Food.Column.NAME, true)
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<Food> getAllFromUser() {
        try {
            return getQueryBuilder()
                    .orderBy(Food.Column.NAME, true)
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                    .and().isNull(BaseServerEntite.Column.SERVER_ID)
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<Food> getAllCommon(Context context) {
        try {
            return getQueryBuilder()
                .orderBy(Food.Column.UPDATED_AT, true)
                .where().eq(Food.Column.LABELS, context.getString(R.string.makla_common))
                .and().isNull(BaseServerEntite.Column.SERVER_ID)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    private void cascadeFoodEaten(Food makla) {
        List<FoodEaten> FoodEatenList = FoodEatenydk.getInstance().getAll(makla);
        for (FoodEaten FoodEaten : FoodEatenList) {
            FoodEatenydk.getInstance().delete(FoodEaten);
        }
    }

    @Override
    public void softDelete(Food entity) {
        cascadeFoodEaten(entity);
        super.softDelete(entity);
    }

    @Override
    public int delete(Food object) {
        cascadeFoodEaten(object);
        return super.delete(object);
    }

    @Override
    public int delete(List<Food> objects) {
        for (Food makla : objects) {
            cascadeFoodEaten(makla);
        }
        return super.delete(objects);
    }

    public Food get(String name) {
        try {
            return getQueryBuilder()
                    .where().eq(Food.Column.NAME, name)
                    .queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return null;
        }
    }

    public List<Food> search(String query, long page) {
        try {
            QueryBuilder<Food, Long> queryBuilder = getQueryBuilder()
                    .orderBy(Food.Column.NAME, true)
                    .orderBy(Food.Column.UPDATED_AT, false)
                    .offset(page * Baseydk.PAGE_SIZE)
                    .limit(Baseydk.PAGE_SIZE);

            boolean hasQuery = query != null && query.length() > 0;
            if (hasQuery) {
                return queryBuilder
                        .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                        .and().like(Food.Column.NAME, new SelectArg("%" + query + "%"))
                        .and().isNull(Food.Column.DELETED_AT)
                        .query();

            } else {
                return queryBuilder
                        .where().eq(Food.Column.LANGUAGE_CODE, Helper.getLanguageCode())
                        .and().isNull(Food.Column.DELETED_AT)
                        .query();
            }
            
        } catch (SQLException exception) {
            Log.e(TAG, exception.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public List<Food> createOrUpdate(SearchResponseydk ydk) {
        List<Food> maklaList = new ArrayList<>();
        Collections.reverse(ydk.products);
        for (Productydk productydk : ydk.products) {
            if (productydk.isValid()) {
                Food makla = parseFromydk(productydk);
                if (makla != null) {
                    maklaList.add(0, makla);
                }
            }
        }
        maklaydk.getInstance().bulkCreateOrUpdate(maklaList);
        return maklaList;
    }

    @Nullable
    private Food parseFromydk(Productydk ydk) {
        if (!ydk.identifier.isJsonPrimitive()) {
            return null;
        }
        String serverId = ydk.identifier.getAsJsonPrimitive().getAsString();
        if (StringUs.isBlank(serverId)) {
            return null;
        }

        Food makla = getByServerId(serverId);
        boolean isNew = makla == null;
        if (isNew) {
            makla = new Food();
        }

        if (isNew || needsUpdate(makla, ydk)) {
            makla.setServerId(serverId);
            makla.setName(ydk.name);
            makla.setBrand(ydk.brand);
            makla.setIngredients(ydk.ingredients != null ? ydk.ingredients.replaceAll("_", "") : null);
            makla.setLabels(ydk.labels);
            makla.setCarbohydrates(ydk.nutritions.carbohydrates);
            makla.setEnergy(ydk.nutritions.energy);
            makla.setFat(ydk.nutritions.fat);
            makla.setFatSaturated(ydk.nutritions.fatSaturated);
            makla.setFiber(ydk.nutritions.fiber);
            makla.setProteins(ydk.nutritions.proteins);
            makla.setSalt(ydk.nutritions.salt);
            makla.setSodium(ydk.nutritions.sodium);
            makla.setSugar(ydk.nutritions.sugar);
            Locale locale = ydk.languageCode != null ? new Locale(ydk.languageCode) : Helper.getLocale();
            makla.setLanguageCode(locale.getLanguage());
        }

        return makla;
    }

    private boolean needsUpdate(Food makla, Productydk ydk) {
        String lastEditDateString = ydk.lastEditDates != null && ydk.lastEditDates.length > 0 ? ydk.lastEditDates[0] : null;
        DateTime lastEditDate = choisirletimeUS.parseFromString(lastEditDateString, Productydk.DATE_FORMAT);
        return lastEditDate != null && makla.getUpdatedAt() != null && makla.getUpdatedAt().isBefore(lastEditDate);
    }
}
