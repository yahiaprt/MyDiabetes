package com.mydiabetesprt.diabetes.future.preference.usin;

import android.view.View;
import android.widget.TextView;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.view.texteditor.LocalisedNumeroEditText;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTimeConstants;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.09.2016.
 */
public class usinViewHolder extends BaseViewHolder<usinListItem> {

    @BindView(R.id.list_item_time_text)
    protected TextView time;

    @BindView(R.id.list_item_time_value)
    public LocalisedNumeroEditText value;

    public usinViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        final usinListItem preference = getListItem();

        if (preference.getInterval() == TimeInterval.CONSTANT) {
            time.setVisibility(View.GONE);
        } else {
            time.setVisibility(View.VISIBLE);
            int hourOfDay = preference.getHourOfDay();
            int target = (preference.getHourOfDay() + preference.getInterval().interval) % DateTimeConstants.HOURS_PER_DAY;
            if (preference.getInterval() == TimeInterval.EVERY_SIX_HOURS) {
                String timeOfDay = getContext().getString(jourtime.toDayTime(hourOfDay).textResourceId);
                time.setText(String.format("%s (%02d - %02d:00)", timeOfDay, hourOfDay, target));
            } else {
                time.setText(String.format("%02d:00 - %02d:00", hourOfDay, target));
            }
        }

        value.setText(preference.getValue() >= 0 ? FloatUs.parseFloat(preference.getValue()) : null);
    }
}
