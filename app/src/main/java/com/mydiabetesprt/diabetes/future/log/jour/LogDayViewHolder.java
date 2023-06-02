package com.mydiabetesprt.diabetes.future.log.jour;

import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogDayViewHolder extends BaseViewHolder<LogDayListItem> {

    @BindView(R.id.day) TextView day;
    @BindView(R.id.weekday) TextView weekDay;

    public LogDayViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        DateTime dateTime = getListItem().getDateTime();

        day.setText(dateTime.toString("dd"));
        weekDay.setText(dateTime.dayOfWeek().getAsShortText());

        // Highlight current jour
        boolean isToday = dateTime.withTimeAtStartOfDay().isEqual(DateTime.now().withTimeAtStartOfDay());
        int textColor =  isToday ?
                ContextCompat.getColor(getContext(), R.color.green) :
                ContextCompat.getColor(getContext(), R.color.gray_dark);
        day.setTextColor(textColor);
        weekDay.setTextColor(textColor);
    }
}
