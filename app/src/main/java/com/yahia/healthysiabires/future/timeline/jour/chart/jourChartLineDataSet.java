package com.yahia.healthysiabires.future.timeline.jour.chart;

import androidx.annotation.ColorInt;

import com.yahia.healthysiabires.partage.view.chart.ChartUs;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 04.11.2017
 */

class jourChartLineDataSet extends LineDataSet {

    jourChartLineDataSet(String label, @ColorInt int color) {
        super(new ArrayList<>(), label);
        setColor(color);
        setLineWidth(ChartUs.LINE_WIDTH);
        setDrawCircles(false);
        setDrawValues(false);
        setDrawHighlightIndicators(false);
    }
}
