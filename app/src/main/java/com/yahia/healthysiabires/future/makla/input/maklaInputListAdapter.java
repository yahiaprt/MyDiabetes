package com.yahia.healthysiabires.future.makla.input;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class maklaInputListAdapter extends BaseAdapter<FoodEaten, maklaInputViewHolder> {

    maklaInputListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public maklaInputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new maklaInputViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement_meal_food_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final maklaInputViewHolder holder, int position) {
        holder.bindData(getItem(position));
        startAnimation(holder.itemView, position);
    }

    float getTotalCarbohydrates() {
        float totalCarbohydrates = 0;
        for (FoodEaten FoodEaten : getItems()) {
            totalCarbohydrates += FoodEaten.getCarbohydrates();
        }
        return totalCarbohydrates;
    }

    boolean hasInput() {
        for (FoodEaten FoodEaten : getItems()) {
            if (FoodEaten.getAmountInGrams() > 0) {
                return true;
            }
        }
        return false;
    }

    private void startAnimation(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        view.startAnimation(animation);
    }
}
