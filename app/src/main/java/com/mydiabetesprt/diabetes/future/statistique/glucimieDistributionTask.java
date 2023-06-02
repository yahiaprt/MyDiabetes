package com.mydiabetesprt.diabetes.future.statistique;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.view.chart.porcentageValueFormatter;
import com.mydiabetesprt.diabetes.future.datetemps.letempsSpan;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.BasesynchronisationTask;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 16.03.2016.
 */

public class glucimieDistributionTask extends BasesynchronisationTask<Void, Void, PieData> {

    private letempsSpan timeSpan;

    public glucimieDistributionTask(Context context, letempsSpan timeSpan, OnAsyncProgressListener<PieData> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
        this.timeSpan = timeSpan;
    }

    @Override
    protected PieData doInBackground(Void... params) {
        List<PieEntry> entries = new ArrayList<>();

        float limitHypo = PreferenceHelper.getInstance().getLimitHypoglycemia();
        float limitHyper = PreferenceHelper.getInstance().getLimitHyperglycemia();

        Interval interval = timeSpan.getInterval(DateTime.now(), -1);
        long targetCount = EntryDao.getInstance().countBetween(interval.getStart(), interval.getEnd(), limitHypo,  limitHyper);
        long hypoCount = EntryDao.getInstance().countBelow(interval.getStart(), interval.getEnd(), limitHypo);
        long hyperCount = EntryDao.getInstance().countAbove(interval.getStart(), interval.getEnd(), limitHyper);

        List<Integer> colors = new ArrayList<>();

        if (targetCount > 0) {
            entries.add(new PieEntry(targetCount, getContext().getString(R.string.target_area), entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (hypoCount > 0) {
            entries.add(new PieEntry(hypoCount, getContext().getString(R.string.hypo), entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.blue));
        }

        if (hyperCount > 0) {
            entries.add(new PieEntry(hyperCount, getContext().getString(R.string.hyper), entries.size()));
            colors.add(ContextCompat.getColor(getContext(), R.color.red));
        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        dataSet.setValueTextSize(13);
        dataSet.setValueFormatter(new porcentageValueFormatter());

        return new PieData(dataSet);
    }
}