package com.yahia.healthysiabires.future.statistique;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.ydk.mesorationydk;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.view.chart.ChartUs;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.future.datetemps.letempsSpan;
import com.yahia.healthysiabires.partage.data.synchronisation.BasesynchronisationTask;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 16.03.2016.
 */

public class MeasurationAverageTask extends BasesynchronisationTask<Void, Void, LineData> {

    private static final String TAG = MeasurationAverageTask.class.getSimpleName();
    private static final float CIRCLE_RADIUS = 5;

    private type category;
    private letempsSpan timeSpan;
    private int dataSetColor;
    private boolean forceDrawing;
    private boolean fillDrawing;

    public MeasurationAverageTask(Context context, type category, letempsSpan timeSpan, boolean forceDrawing, boolean fillDrawing, OnAsyncProgressListener<LineData> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
        this.category = category;
        this.timeSpan = timeSpan;
        this.forceDrawing = forceDrawing;
        this.fillDrawing = fillDrawing;
        this.dataSetColor = ContextCompat.getColor(context, R.color.green_light);
    }

    @Override
    protected LineData doInBackground(Void... params) {
        List<Entry> entries = new ArrayList<>();

        DateTime endDateTime = DateTime.now().withTime(23, 59, 59, 999);
        DateTime startDateTime = timeSpan.getInterval(endDateTime, -1).getStart();
        startDateTime = startDateTime.withTimeAtStartOfDay();

        int index = 0;
        float highestValue = 0;
        DateTime intervalStart = startDateTime;
        while (!intervalStart.isAfter(endDateTime)) {
            DateTime intervalEnd = timeSpan.getStep(intervalStart, 1).minusDays(1);
            mesoration measurement = mesorationydk.getInstance(category.toClass()).getAvgMeasurement(category, new Interval(intervalStart, intervalEnd));
            if (measurement != null) {
                for (Float avg : measurement.getValues()) {
                    if (FloatUs.isValid(avg) && avg > 0) {
                        float yValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, avg);
                        entries.add(new Entry(index, yValue));
                        if (yValue > highestValue) {
                            highestValue = yValue;
                        }
                    }
                }
            }
            intervalStart = timeSpan.getStep(intervalStart, 1);
            index++;
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if (entries.size() > 0) {
            LineDataSet dataSet = new LineDataSet(entries, "Blood sugar average per jour");
            dataSet.setColor(dataSetColor);
            dataSet.setLineWidth(fillDrawing ? 0 : ChartUs.LINE_WIDTH);
            dataSet.setCircleColor(dataSetColor);
            dataSet.setCircleRadius(CIRCLE_RADIUS);
            dataSet.setDrawCircles(entries.size() <= 1);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(fillDrawing);
            dataSet.setFillColor(dataSetColor);
            dataSets.add(dataSet);
        }

        // Workaround to set visible area
        if (forceDrawing) {
            if (category == type.BLOODSUGAR) {
                float targetValue = PreferenceHelper.getInstance().
                        formatDefaultToCustomUnit(type.BLOODSUGAR,
                                PreferenceHelper.getInstance().getTargetValue());
                targetValue = targetValue * 2;
                if (highestValue == 0 || highestValue < targetValue) {
                    highestValue = targetValue;
                }
            }
            List<com.github.mikephil.charting.data.Entry> entriesMaximum = new ArrayList<>();
            entriesMaximum.add(new com.github.mikephil.charting.data.Entry(-1, highestValue));
            LineDataSet dataSetMaximum = new LineDataSet(entriesMaximum, "Fake");
            dataSetMaximum.setColor(Color.TRANSPARENT);
            dataSets.add(dataSetMaximum);
        }

        try {
            return new LineData(dataSets);
        } catch (IllegalArgumentException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }
}