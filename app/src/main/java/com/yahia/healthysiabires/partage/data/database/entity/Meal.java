package com.yahia.healthysiabires.partage.data.database.entity;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable
public class Meal extends mesoration {

    public class Column extends mesoration.Column {
        public static final String CARBOHYDRATES = "carbohydrates";
    }

    @DatabaseField(columnName = Column.CARBOHYDRATES)
    private float carbohydrates;

    @ForeignCollectionField
    private ForeignCollection<FoodEaten> FoodEaten;

    private List<FoodEaten> FoodEatenCache;

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public ForeignCollection<FoodEaten> getFoodEaten() {
        return FoodEaten;
    }

    public void setFoodEaten(ForeignCollection<FoodEaten> FoodEaten) {
        this.FoodEaten = FoodEaten;
    }

    private float getTotalCarbohydrates() {
        float carbohydrates = getCarbohydrates();
        if (FoodEaten != null) {
            for (FoodEaten eaten : FoodEaten) {
                carbohydrates += eaten.getCarbohydrates();
            }
        }
        return carbohydrates;
    }

    @Override
    public type getCategory() {
        return type.MEAL;
    }

    @Override
    public float[] getValues() {
        return new float[] { carbohydrates };
    }

    @Override
    public void setValues(float... values) {
        if (values.length > 0) {
            carbohydrates = values[0];
        }
    }

    public List<FoodEaten> getFoodEatenCache() {
        if (FoodEatenCache == null) {
            FoodEatenCache = new ArrayList<>();
        }
        return FoodEatenCache;
    }

    public void setFoodEatenCache(List<FoodEaten> FoodEatenCache) {
        this.FoodEatenCache = FoodEatenCache;
    }

    @NonNull
    @Override
    public String toString() {
        return PreferenceHelper.getInstance().getMeasurementForUi(getCategory(), getTotalCarbohydrates());
    }
}
