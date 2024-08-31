package com.yahia.healthysiabires.future.makla.detail.history;

import android.view.View;
import android.widget.TextView;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.future.Entrer.editor.EntryEditActivity;
import com.yahia.healthysiabires.partage.Helper;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.partage.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class maklaHistoryViewHolder extends BaseViewHolder<FoodEaten> implements View.OnClickListener {

    @BindView(R.id.list_item_makla_eaten_date_time) TextView dateTime;
    @BindView(R.id.list_item_makla_eaten_amount) TextView amount;

    maklaHistoryViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        FoodEaten FoodEaten = getListItem();
        boolean hasDateTime = FoodEaten.getMeal() != null && FoodEaten.getMeal().getEntry() != null;
        if (hasDateTime) {
            DateTime FoodEatenDateTime = FoodEaten.getMeal().getEntry().getDate();
            dateTime.setText(String.format("%s %s",
                    Helper.getDateFormat().print(FoodEatenDateTime),
                    Helper.getTimeFormat().print(FoodEatenDateTime)));
        } else {
            dateTime.setText(null);
        }
        amount.setText(String.format("%s g", FloatUs.parseFloat(FoodEaten.getAmountInGrams())));
    }

    @Override
    public void onClick(View view) {
        EntryEditActivity.show(getContext(), getListItem().getMeal().getEntry());
    }
}
