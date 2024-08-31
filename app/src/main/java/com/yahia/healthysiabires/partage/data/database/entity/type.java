package com.yahia.healthysiabires.partage.data.database.entity;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.yahia.healthysiabires.R;
public enum type {

    BLOODSUGAR(glycemie.class, 1, R.string.bloodsugar, R.drawable.ic_category_bloodsugar, R.drawable.background_primary),
    INSULIN(Insulin.class, 2, R.string.insulin, R.drawable.ic_category_insulin, R.drawable.background_primary),
    MEAL(Meal.class, 3, R.string.meal, R.drawable.ic_category_meal, R.drawable.background_primary),
    ACTIVITY(Activity.class, 4, R.string.activity, R.drawable.ic_category_activity, R.drawable.background_primary),
    HBA1C(HbA1c.class, 5, R.string.hba1c, R.drawable.ic_category_hba1c, R.drawable.background_primary),
    WEIGHT(poits.class, 6, R.string.weight, R.drawable.ic_category_weight, R.drawable.background_primary),
    PULSE(Pulsation.class, 7, R.string.pulse, R.drawable.ic_category_pulse, R.drawable.background_primary),
    PRESSURE(tension.class, 8, R.string.pressure, R.string.pressure_acronym, R.drawable.ic_category_pressure, R.drawable.background_primary),
    OXYGEN_SATURATION(OxySaturation.class, 9, R.string.oxygen_saturation, R.string.oxygen_saturation_acronym, R.drawable.ic_category_oxygen_saturation, R.drawable.background_primary);

    private Class clazz;
    private int stableId;
    private int stringResId;
    private int stringAcronymResId;
    private int iconImageResId;
    private int showcaseImageResId;

    type(
        Class clazz,
        int stableId,
        @StringRes int stringResId,
        @StringRes int stringAcronymResId,
        @DrawableRes int iconImageResId,
        @DrawableRes int showcaseImageResId
    ) {
        this.clazz = clazz;
        this.stableId = stableId;
        this.stringResId = stringResId;
        this.stringAcronymResId = stringAcronymResId;
        this.iconImageResId = iconImageResId;
        this.showcaseImageResId = showcaseImageResId;
    }

    type(
        Class clazz,
        int stableId,
        @StringRes int stringResId,
        @DrawableRes int iconImageResId,
        @DrawableRes int showcaseImageResId
    ) {
        this(clazz, stableId, stringResId, stringResId, iconImageResId, showcaseImageResId);
    }

    public <M extends mesoration> Class<M> toClass() {
        return clazz;
    }

    public boolean stackValues() {
        return this == INSULIN || this == MEAL || this == ACTIVITY;
    }

    public int getStableId() {
        return stableId;
    }

    public int getStringResId() {
        return stringResId;
    }

    public int getStringAcronymResId() {
        return stringAcronymResId;
    }

    @DrawableRes
    public int getIconImageResourceId() {
        return iconImageResId;
    }

    @DrawableRes
    public int getShowcaseImageResourceId() {
        return showcaseImageResId;
    }

    public boolean isOptional() {
        return this != BLOODSUGAR;
    }

    public static type fromStableId(int stableId) {
        for (type category : values()) {
            if (category.stableId == stableId) {
                return category;
            }
        }
        return null;
    }
}
