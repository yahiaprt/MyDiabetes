package com.mydiabetesprt.diabetes.future.timeline.jour.tablo;

import android.view.View;
import android.widget.TextView;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;

public class CategoryValueViewHolder extends BaseViewHolder<CategoryValueListItem> implements View.OnClickListener {

    @BindView(R.id.category_value) TextView valueView;

    CategoryValueViewHolder(View view) {
        super(view);

        valueView.setOnClickListener(this);
    }

    @Override
    public void bindData() {
        valueView.setLines(1);
        CategoryValueListItem listItem = getListItem();
        String value = listItem.print();

        int lines = StringUtils.countMatches(value, "\n") + 1;
        valueView.setLines(lines);

        if (value.length() > 0) {
            valueView.setText(value);
            valueView.setClickable(true);
        } else {
            valueView.setText(null);
            valueView.setClickable(false);
        }
    }



        @Override
    public void onClick(View view) {
        // TODO: Use mesoration.toString() instead
        type category = getListItem().getCategory();
        String unitAcronym = PreferenceHelper.getInstance().getUnitName(category);
        ViewUs.showToast(getContext(), unitAcronym);

     }
}
