package com.mydiabetesprt.diabetes.future.log.empty;

import android.view.View;
import android.widget.TextView;

import com.mydiabetesprt.diabetes.future.Entrer.editor.EntryEditActivity;
import com.mydiabetesprt.diabetes.R;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class LogEmptyViewHolder extends BaseViewHolder<LogEmptyListItem> implements View.OnClickListener {

    @BindView(R.id.empty) TextView textView;

    public LogEmptyViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DateTime now = DateTime.now();
        DateTime dateTime = getListItem().getDateTime()
                .withHourOfDay(now.hourOfDay().get())
                .withMinuteOfHour(now.minuteOfHour().get())
                .withSecondOfMinute(now.secondOfMinute().get())
                .withMillisOfSecond(now.millisOfSecond().get());
        EntryEditActivity.show(getContext(), dateTime);
    }
}
