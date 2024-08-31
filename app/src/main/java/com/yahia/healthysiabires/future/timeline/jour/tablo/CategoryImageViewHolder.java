package com.yahia.healthysiabires.future.timeline.jour.tablo;

import android.view.View;
import android.widget.ImageView;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.view.image.ImageLoader;
import com.yahia.healthysiabires.partage.view.ViewUs;
import com.yahia.healthysiabires.partage.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

class CategoryImageViewHolder extends BaseViewHolder<CategoryImageListItem> implements View.OnClickListener {

    @BindView(R.id.category_image) ImageView imageView;

    CategoryImageViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    public void bindData() {
        int categoryImageResourceId = getListItem().getCategory().getIconImageResourceId();
        if (categoryImageResourceId > 0) {
            ImageLoader.getInstance().load(categoryImageResourceId, imageView);
        }
    }

    @Override
    public void onClick(View view) {
        type category = getListItem().getCategory();
        ViewUs.showToast(getContext(), getContext().getString(category.getStringResId()));
    }
}
