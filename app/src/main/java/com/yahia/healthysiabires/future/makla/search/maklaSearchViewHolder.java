package com.yahia.healthysiabires.future.makla.search;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.ui.FoodSelectedEvent;
import com.yahia.healthysiabires.partage.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class maklaSearchViewHolder extends BaseViewHolder<maklaSearchListItem> implements View.OnClickListener {

    @BindView(R.id.makla_name) TextView name;
    @BindView(R.id.makla_brand) TextView brand;
    @BindView(R.id.makla_carbohydrates) TextView carbohydrates;
    @BindView(R.id.makla_recent) ImageView recentIndicator;

    maklaSearchViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        Food makla = getListItem().getmakla();
        name.setText(makla.getName());
        brand.setText(makla.getBrand());
        brand.setVisibility(makla.getBrand() != null && makla.getBrand().length() > 0 ? View.VISIBLE : View.GONE);
        carbohydrates.setText(makla.getValueForUi());
        recentIndicator.setVisibility(getListItem().getFoodEaten() != null ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        ets.post(new FoodSelectedEvent(getListItem().getmakla(), view));
    }
}
