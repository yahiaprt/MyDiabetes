package com.mydiabetesprt.diabetes.partage.data.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mydiabetesprt.diabetes.future.export.Backupable;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.format.DateTimeFormat;

public class FoodEaten extends BaseEntite implements Backupable {

    public static final String BACKUP_KEY = "FoodEaten";

    public class Column extends BaseEntite.Column {
        public static final String AMOUNT_IN_GRAMS = "amountInGrams";
        public static final String MEAL = "meal";
        public static final String makla = "makla";
    }

    @DatabaseField(columnName = Column.AMOUNT_IN_GRAMS)
    private float amountInGrams;

    @DatabaseField(columnName = Column.MEAL, foreign = true, foreignAutoRefresh = true)
    private Meal meal;

    @DatabaseField(columnName = Column.makla, foreign = true, foreignAutoRefresh = true)
    private Food makla;

    public float getAmountInGrams() {
        return amountInGrams;
    }

    public void setAmountInGrams(float amountInGrams) {
        this.amountInGrams = amountInGrams;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Food getmakla() {
        return makla;
    }

    public void setmakla(Food makla) {
        this.makla = makla;
    }

    public float getCarbohydrates() {
        return getmakla() != null ? getAmountInGrams() * getmakla().getCarbohydrates() / 100 : 0;
    }

    @Nullable
    public String print() {
        int amountEaten = (int) getAmountInGrams();
        if (makla != null && amountEaten > 0) {
            return String.format("%s (%d g)", makla.getName(), amountEaten);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s: %f grams (updated: %s)",
            makla.getName(),
            amountInGrams,
            DateTimeFormat.mediumDateTime().print(getUpdatedAt())
        );
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        return new String[]{makla.getName(), Float.toString(amountInGrams)};
    }
}
