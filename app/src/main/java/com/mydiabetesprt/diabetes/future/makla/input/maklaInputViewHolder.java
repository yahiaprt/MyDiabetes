package com.mydiabetesprt.diabetes.future.makla.input;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.ui.FoodEatenRemovedEvent;
import com.mydiabetesprt.diabetes.partage.event.ui.FoodEatenUpdatedEvent;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;
import com.mydiabetesprt.diabetes.partage.view.resource.ColorUs;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

class maklaInputViewHolder extends BaseViewHolder<FoodEaten> {

    @BindView(R.id.makla_name) TextView name;
    @BindView(R.id.makla_carbohydrates) TextView value;
    @BindView(R.id.makla_amount) AppCompatButton amount;
    @BindView(R.id.makla_delete) ImageView delete;

    maklaInputViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        final FoodEaten FoodEaten = getListItem();
        final Food makla = FoodEaten.getmakla();

        name.setText(makla.getName());
        value.setText(String.format("%s %s",
                makla.getValueForUi(),
                PreferenceHelper.getInstance().getLabelForMealPer100g(getContext())));

        amount.setOnClickListener(view -> {
            if (getContext() instanceof AppCompatActivity) {
                showNumberPicker((AppCompatActivity) getContext());
            }
        });

        delete.setOnClickListener(view -> ets.post(new FoodEatenRemovedEvent(FoodEaten, getAdapterPosition())));
        delete.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), makla.getName()));

        updateUi();
    }

    private void showNumberPicker(AppCompatActivity activity) {
        ViewUs.showNumberPicker(activity, R.string.grams_milliliters_acronym, getAmountFromButton(), 1, 10000, (reference, number, decimal, isNegative, fullNumber) -> {
            FoodEaten FoodEaten = getListItem();
            FoodEaten.setAmountInGrams(number.floatValue());
            ets.post(new FoodEatenUpdatedEvent(FoodEaten, getAdapterPosition()));
        });
    }

    @SuppressWarnings("RestrictedApi")
    private void updateUi() {
        FoodEaten FoodEaten = getListItem();
        boolean isSet = FoodEaten.getAmountInGrams() > 0;
        String text = isSet ?
                String.format("%s %s", FloatUs.parseFloat(getListItem().getAmountInGrams()), getContext().getString(R.string.grams_milliliters_acronym)) :
                getContext().getString(R.string.amount);
        int backgroundColor = isSet ? ColorUs.getBackgroundTertiary(getContext()) : ColorUs.getPrimaryColor(getContext());
        int textColor = isSet ? ColorUs.getTextColorPrimary(getContext()) : Color.WHITE;
        amount.setText(text);
        amount.setSupportBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        amount.setTextColor(textColor);
    }

    private int getAmountFromButton() {
        String label = amount.getText().toString();
        if (label.length() > 0) {
            String numberLabel = label.substring(0, label.indexOf(" "));
            try {
                return (int) FloatUs.parseNumber(numberLabel);
            } catch (NumberFormatException exception) {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
