package com.yahia.healthysiabires.future.Entrer.editor.measuration;

import android.content.Context;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.future.makla.input.maklaInputView;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class MealView extends mesorationAbstractView<Meal> {

    @BindView(R.id.list_item_measurement_meal_makla_list)
    maklaInputView maklaInputView;

    public MealView(Context context) {
        super(context, type.MEAL);
    }

    public MealView(Context context, Meal meal) {
        super(context, meal);
    }

    public MealView(Context context, Food makla) {
        super(context, makla);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_meal;
    }

    @Override
    protected void initLayout() {
        maklaInputView.addItem(makla);
    }

    @Override
    protected void setValues() {
        maklaInputView.setupWithMeal(measurement);
    }

    @Override
    protected boolean isValid() {
        return maklaInputView.isValid();
    }

    @Override
    public mesoration getMeasurement() {
        return maklaInputView.getMeal();
    }
}