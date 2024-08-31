package com.yahia.healthysiabires.future.makla.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yahia.healthysiabires.partage.data.database.entity.Food;

import java.util.HashMap;
import java.util.Map;

public class nutritionInputLayout extends LinearLayout {

    public nutritionInputLayout(Context context) {
        super(context);
        init();
    }

    public nutritionInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public nutritionInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void addnutrition(@NonNull Food.nutrition nutrition, @Nullable Float value) {
        addView(new nutritionInputView(getContext(), nutrition, value));
    }

    public Map<Food.nutrition, Float> getValues() {
        Map<Food.nutrition, Float> values = new HashMap<>();
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            if (view instanceof nutritionInputView) {
                nutritionInputView nutritionInputView = (nutritionInputView) view;
                values.put(nutritionInputView.getnutrition(), nutritionInputView.getValue());
            }
        }
        return values;
    }

    public nutritionInputView getInputView(Food.nutrition nutrition) {
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            if (view instanceof nutritionInputView) {
                nutritionInputView nutritionInputView = (nutritionInputView) view;
                if (nutritionInputView.getnutrition() == nutrition) {
                    return nutritionInputView;
                }
            }
        }
        return null;
    }
}