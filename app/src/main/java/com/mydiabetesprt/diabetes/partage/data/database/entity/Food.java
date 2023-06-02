package com.mydiabetesprt.diabetes.partage.data.database.entity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.future.export.Backupable;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import static com.mydiabetesprt.diabetes.partage.data.database.entity.Food.Column.FAT_SATURATED;
import static com.mydiabetesprt.diabetes.partage.data.database.entity.Food.Column.PROTEINS;
import static com.mydiabetesprt.diabetes.partage.data.database.entity.Food.Column.SUGAR;

public class Food extends BaseServerEntite implements Backupable {

    public static final String BACKUP_KEY = "food";

    @SuppressWarnings("WeakerAccess")
    public class Column extends BaseServerEntite.Column {
        public static final String NAME = "name";
        public static final String BRAND = "brand";
        public static final String INGREDIENTS = "ingredients";
        public static final String LABELS = "labels";
        public static final String CARBOHYDRATES = "carbohydrates";
        public static final String ENERGY = "energy";
        public static final String FAT = "fat";
        public static final String FAT_SATURATED = "fatSaturated";
        public static final String FIBER = "fiber";
        public static final String PROTEINS = "proteins";
        public static final String SALT = "salt";
        public static final String SODIUM = "sodium";
        public static final String SUGAR = "sugar";
        public static final String LANGUAGE_CODE = "languageCode";
        public static final String makla_EATEN = "FoodEaten";
    }

    public enum nutrition {
        CARBOHYDRATES(R.string.carbohydrates),
        SUGAR(R.string.sugar),
        ENERGY(R.string.energy),
        FAT(R.string.fat),
        FAT_SATURATED(R.string.fat_saturated),
        FIBER(R.string.fiber),
        PROTEINS(R.string.proteins),
        SALT(R.string.salt),
        SODIUM(R.string.sodium);

        private int textResId;

        nutrition(@StringRes int textResId) {
            this.textResId = textResId;
        }

        public String getLabel(Context context) {
            return context.getString(textResId);
        }

        public @StringRes int getUnitResId() {
            switch (this) {
                case ENERGY: return R.string.energy_acronym;
                case SODIUM: return R.string.milligrams_acronym;
                default: return R.string.grams_acronym;
            }
        }

        public Float getValue(Food makla) {
            switch (this) {
                case CARBOHYDRATES: return makla.getCarbohydrates();
                case ENERGY: return makla.getEnergy();
                case FAT: return makla.getFat();
                case FAT_SATURATED: return makla.getFatSaturated();
                case FIBER: return makla.getFiber();
                case PROTEINS: return makla.getProteins();
                case SALT: return makla.getSalt();
                case SODIUM: return makla.getSodium() != null ? makla.getSodium() * 1000 : null;
                case SUGAR: return makla.getSugar();
                default: return null;
            }
        }

        public void setValue(Food makla, float value) {
            switch (this) {
                case CARBOHYDRATES: makla.setCarbohydrates(value); break;
                case ENERGY: makla.setEnergy(value); break;
                case FAT: makla.setFat(value); break;
                case FAT_SATURATED: makla.setFatSaturated(value); break;
                case FIBER: makla.setFiber(value); break;
                case PROTEINS: makla.setProteins(value); break;
                case SALT: makla.setSalt(value); break;
                case SODIUM: makla.setSodium(value > 0 ? value / 1000 : value); break;
                case SUGAR: makla.setSugar(value); break;
                default: Log.e(nutrition.class.getSimpleName(), "Unsupported nutrition for applyValue(): " + this);
            }
        }
    }

    @DatabaseField(columnName = Column.NAME)
    private String name;

    @DatabaseField(columnName = Column.BRAND)
    private String brand;

    @DatabaseField(columnName = Column.INGREDIENTS)
    private String ingredients;

    @DatabaseField(columnName = Column.LABELS)
    private String labels;

    @DatabaseField(columnName = Column.CARBOHYDRATES, defaultValue = "-1")
    private Float carbohydrates;

    @DatabaseField(columnName = Column.ENERGY, defaultValue = "-1")
    private Float energy;

    @DatabaseField(columnName = Column.FAT, defaultValue = "-1")
    private Float fat;

    @DatabaseField(columnName = FAT_SATURATED, defaultValue = "-1")
    private Float fatSaturated;

    @DatabaseField(columnName = Column.FIBER, defaultValue = "-1")
    private Float fiber;

    @DatabaseField(columnName = PROTEINS, defaultValue = "-1")
    private Float proteins;

    @DatabaseField(columnName = Column.SALT, defaultValue = "-1")
    private Float salt;

    @DatabaseField(columnName = Column.SODIUM, defaultValue = "-1")
    private Float sodium;

    @DatabaseField(columnName = SUGAR, defaultValue = "-1")
    private Float sugar;

    @DatabaseField(columnName = Column.LANGUAGE_CODE)
    private String languageCode;

    @ForeignCollectionField(columnName = Column.makla_EATEN)
    private ForeignCollection<FoodEaten> FoodEaten;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Float getEnergy() {
        return energy;
    }

    public void setEnergy(Float energy) {
        this.energy = energy;
    }

    public Float getFat() {
        return fat;
    }

    public void setFat(Float fat) {
        this.fat = fat;
    }

    public Float getFatSaturated() {
        return fatSaturated;
    }

    public void setFatSaturated(Float fatSaturated) {
        this.fatSaturated = fatSaturated;
    }

    public Float getFiber() {
        return fiber;
    }

    public void setFiber(Float fiber) {
        this.fiber = fiber;
    }

    public Float getProteins() {
        return proteins;
    }

    public void setProteins(Float proteins) {
        this.proteins = proteins;
    }

    public Float getSalt() {
        return salt;
    }

    public void setSalt(Float salt) {
        this.salt = salt;
    }

    public Float getSodium() {
        return sodium;
    }

    public void setSodium(Float sodium) {
        this.sodium = sodium;
    }

    public Float getSugar() {
        return sugar;
    }

    public void setSugar(Float sugar) {
        this.sugar = sugar;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public ForeignCollection<FoodEaten> getFoodEaten() {
        return FoodEaten;
    }

    public void setFoodEaten(ForeignCollection<FoodEaten> FoodEaten) {
        this.FoodEaten = FoodEaten;
    }

    public boolean isBrandedmakla() {
        return !TextUtils.isEmpty(brand);
    }

    public String getValueForUi() {
        if (carbohydrates != null) {
            float valueFormatted = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.MEAL, carbohydrates);
            return FloatUs.parseFloat(valueFormatted);
        } else {
            return "";
        }
    }

    @Override
    public String getKeyForBackup() {
        return BACKUP_KEY;
    }

    @Override
    public String[] getValuesForBackup() {
        return new String[]{name, brand, ingredients, Float.toString(carbohydrates)};
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
