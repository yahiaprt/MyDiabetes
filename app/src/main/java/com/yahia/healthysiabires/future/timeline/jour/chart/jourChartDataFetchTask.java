package com.yahia.healthysiabires.future.timeline.jour.chart;

import android.content.Context;

import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.synchronisation.BasesynchronisationTask;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 05.11.2017
 */
class jourChartDataFetchTask extends BasesynchronisationTask<Void, Void, jourChartData> {

    private DateTime day;

    jourChartDataFetchTask(Context context, OnAsyncProgressListener<jourChartData> onAsyncProgressListener, DateTime day) {
        super(context, onAsyncProgressListener);
        this.day = day;
    }

    @Override
    protected jourChartData doInBackground(Void... params) {
        List<mesoration> values = new ArrayList<>();
        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
        if (entries != null && entries.size() > 0) {
            for (Entry entry : entries) {
                // TODO: Improve performance by using transaction / bulk fetch
                List<mesoration> measurements = EntryDao.getInstance().getMeasurements(entry, new type[] { type.BLOODSUGAR });
                values.addAll(measurements);
            }
        }
        return new jourChartData(getContext(), values);
    }
}