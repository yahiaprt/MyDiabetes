package com.mydiabetesprt.diabetes.future.makla.detail.nutrition;

import android.view.View;
import android.widget.TextView;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;

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
