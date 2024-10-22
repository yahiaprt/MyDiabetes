package com.yahia.healthysiabires.future.makla.detail.nutrition;

import android.view.View;
import android.widget.TextView;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class nutritionViewHolder extends BaseViewHolder<nutritionListItem> {

    @BindView(R.id.list_item_nutrition_label) TextView label;
    @BindView(R.id.list_item_nutrition_value) TextView value;

    nutritionViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        nutritionListItem listItem = getListItem();
        label.setText(listItem.getLabel());
        value.setText(listItem.getValue());
    }
}
