package com.yahia.healthysiabires.future.export;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

class ExportCategoryViewHolder extends BaseViewHolder<ExportCategoryListItem> {

    @BindView(R.id.category_image) ImageView categoryImageView;
    @BindView(R.id.category_checkbox) CheckBox categoryCheckBox;
    @BindView(R.id.extra_checkbox) CheckBox extraCheckBox;

    ExportCategoryViewHolder(View view) {
        super(view);
        init();
    }

    private void init() {
        categoryCheckBox.setOnCheckedChangeListener((checkBox, isChecked) -> {
            getListItem().setCategorySelected(isChecked);
            extraCheckBox.setEnabled(isChecked);
        });
        extraCheckBox.setOnCheckedChangeListener((checkBox, isChecked) -> getListItem().setExtraSelected(isChecked));
    }

    @Override
    protected void bindData() {
        ExportCategoryListItem item = getListItem();
        type category = item.getCategory();

        categoryImageView.setImageResource(category.getIconImageResourceId());
        categoryCheckBox.setText(getContext().getString(category.getStringResId()));
        categoryCheckBox.setChecked(item.isCategorySelected());

        String extraTitle;
        switch (category) {
            case BLOODSUGAR:
                extraTitle = getContext().getString(R.string.highlight_limits);
                break;
            case INSULIN:
                extraTitle = getContext().getString(R.string.insulin_split);
                break;
            case MEAL:
                extraTitle = getContext().getString(R.string.makla_append);
                break;
            default:
                extraTitle = null;
        }
        extraCheckBox.setText(extraTitle);
        extraCheckBox.setEnabled(item.isCategorySelected());
        extraCheckBox.setChecked(item.isExtraSelected());
        extraCheckBox.setVisibility(extraTitle != null ? View.VISIBLE : View.GONE);
    }
}
