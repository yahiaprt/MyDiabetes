package com.mydiabetesprt.diabetes.future.timeline.jour.chart;

import android.content.Context;

import androidx.annotation.ColorInt;

import com.yahia.healthysiabires.R;import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 04.11.2017
 */

class jourChartScatterDataSet extends ScatterDataSet {

    jourChartScatterDataSet(Context context, String label, @ColorInt int color) {
        super(new ArrayList<Entry>(), label);
        setColor(color);
        setScatterShapeSize(context.getResources().getDimension(R.dimen.chart_scatter_size));
        setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        setDrawValues(false);
        setDrawHighlightIndicators(false);
    }
}
