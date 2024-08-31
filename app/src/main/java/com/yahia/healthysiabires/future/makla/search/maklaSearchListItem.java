package com.yahia.healthysiabires.future.makla.search;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;

/**
 * Created by Filip on 10.07.2015.
 */
public class maklaSearchListItem {

    private Food makla;
    private FoodEaten FoodEaten;

    maklaSearchListItem(FoodEaten FoodEaten) {
        this.makla = FoodEaten.getmakla();
        this.FoodEaten = FoodEaten;
    }

    maklaSearchListItem(Food makla) {
        this.makla = makla;
        this.FoodEaten = null;
    }

    public Food getmakla() {
        return makla;
    }

    public void setmakla(Food makla) {
        this.makla = makla;
    }

    public FoodEaten getFoodEaten() {
        return FoodEaten;
    }

    public void setFoodEaten(FoodEaten FoodEaten) {
        this.FoodEaten = FoodEaten;
    }

    @Override
    public boolean equals(Object item) {
        return item instanceof maklaSearchListItem && makla.equals(((maklaSearchListItem) item).getmakla());
    }

    @NonNull
    @Override
    public String toString() {
        return makla.toString();
    }
}
