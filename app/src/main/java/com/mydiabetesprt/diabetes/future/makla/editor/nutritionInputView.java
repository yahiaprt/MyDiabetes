package com.mydiabetesprt.diabetes.future.makla.editor;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.view.texteditor.StickHintInput;

@SuppressLint("ViewConstructor")
public class nutritionInputView extends StickHintInput {

    private Food.nutrition nutrition;
    private Float initialValue;

    public nutritionInputView(Context context, @NonNull Food.nutrition nutrition, @Nullable Float value) {
        super(context);
        this.nutrition = nutrition;
        this.initialValue = value;
        init();
    }

    @NonNull
    public Food.nutrition getnutrition() {
        return nutrition;
    }

    @Nullable
    public Float getValue() {
        return getInputView().getNumber();
    }

    public void setValue(float value) {
        getInputView().setText(FloatUs.parseFloat(value));
    }

    private void init() {
        setHint(String.format("%s %s %s",
            nutrition.getLabel(getContext()),
            getContext().getString(R.string.in),
            getContext().getString(nutrition.getUnitResId())
        ));
        if (initialValue != null && initialValue >= 0) {
            setValue(initialValue);
        }
    }
}
