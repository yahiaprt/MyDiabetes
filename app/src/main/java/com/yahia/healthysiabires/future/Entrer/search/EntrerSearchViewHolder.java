package com.yahia.healthysiabires.future.Entrer.search;

import android.view.View;

import com.yahia.healthysiabires.future.log.entry.LogEntryViewHolder;
import com.yahia.healthysiabires.partage.Helper;

import org.joda.time.DateTime;

class EntrerSearchViewHolder extends LogEntryViewHolder {

    EntrerSearchViewHolder(View view, EntrerSearchListAdapter.OnSearchItemClickListener listener) {
        super(view, listener);
    }

    @Override
    public void bindData() {
        super.bindData();

        rootLayout.setPadding(rootLayout.getPaddingRight(), rootLayout.getPaddingTop(), rootLayout.getPaddingRight(), rootLayout.getPaddingBottom());

        DateTime dateTime = getListItem().getDateTime();
        dateTimeView.setText(String.format("%s, %s %s",
                dateTime.dayOfWeek().getAsShortText(),
                Helper.getDateFormat().print(dateTime),
                Helper.getTimeFormat().print(dateTime)));
    }
}