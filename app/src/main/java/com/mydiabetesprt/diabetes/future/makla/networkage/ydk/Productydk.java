package com.mydiabetesprt.diabetes.future.makla.networkage.ydk;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.mydiabetesprt.diabetes.partage.data.premier.StringUs;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class Productydk {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    // Workaround: Sometimes a Number, sometimes a String
    @SerializedName("sortkey")
    public JsonElement identifier;

    @SerializedName("lang")
    public String languageCode;

    @SerializedName("product_name")
    public String name;

    @SerializedName("brands")
    public String brand;

    @SerializedName("ingredients_text")
    public String ingredients;

    @SerializedName("labels")
    public String labels;

    @SerializedName("nutriments")
    public Nutrientsydk nutritions;

    @SerializedName("last_edit_dates_tags")
    public String[] lastEditDates;

    public boolean isValid() {
        boolean hasName = !StringUs.isBlank(name);
        boolean hasnutritions = nutritions != null && nutritions.carbohydrates != null;
        return hasName && hasnutritions;
    }
}
