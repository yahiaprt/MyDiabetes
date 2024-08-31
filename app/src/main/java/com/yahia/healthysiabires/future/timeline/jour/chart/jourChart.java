package com.yahia.healthysiabires.future.timeline.jour.chart;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.future.datetemps.choisirletimeUS;
import com.yahia.healthysiabires.future.Entrer.editor.EntryEditActivity;
import com.yahia.healthysiabires.partage.view.chart.ChartUs;
import com.yahia.healthysiabires.partage.view.resource.ColorUs;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 * Created by Filip on 07.07.2015.
 */
public class jourChart extends CombinedChart implements OnChartValueSelectedListener {

    private static final float TAP_THRESHOLD_IN_DP = 24;
    private static final float Y_MAX_VALUE_DEFAULT = 200;
    private static final float Y_MAX_VALUE_OFFSET = 20;

    private DateTime day;

    public jourChart(Context context) {
        super(context);
        setup();
    }

    public jourChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        if (!isInEditMode()) {
            ChartUs.setChartDefaultStyle(this, type.BLOODSUGAR);

            @ColorInt int textColor = ColorUs.getTextColorPrimary(getContext());
            @ColorInt int gridColor = ColorUs.getBackgroundTertiary(getContext());
            getAxisLeft().setTextColor(textColor);
            getAxisLeft().setGridColor(gridColor);
            getAxisLeft().setGridLineWidth(1f);
            getXAxis().setGridLineWidth(1f);
            getXAxis().setGridColor(gridColor);
            getXAxis().setTextColor(textColor);
            getXAxis().setValueFormatter((value, axis) -> {
                int minute = (int) value;
                int hour = minute / DateTimeConstants.MINUTES_PER_HOUR;
                return hour < DateTimeConstants.HOURS_PER_DAY ? Integer.toString(hour) : "";
            });
            getXAxis().setAxisMinimum(0);
            getXAxis().setAxisMaximum(DateTimeConstants.MINUTES_PER_DAY);
            getXAxis().setLabelCount((DateTimeConstants.HOURS_PER_DAY / 2) + 1, true);

            // Offsets are calculated manually
            float leftOffset = getContext().getResources().getDimension(R.dimen.chart_offset_left);
            float bottomOffset = getContext().getResources().getDimension(R.dimen.chart_offset_bottom);
            setViewPortOffsets(leftOffset, 0, 0, bottomOffset);

            setMaxHighlightDistance(TAP_THRESHOLD_IN_DP);
            setOnChartValueSelectedListener(this);
            setDragEnabled(false);

            setDay(DateTime.now());
        }
    }

    private void update() {
        new jourChartDataFetchTask(getContext(), data -> {
            if (ViewCompat.isAttachedToWindow(jourChart.this)) {
                clear();
                setData(data);

                // Identify max value manually because data.getYMax does not work when combining scatter with line chart
                float yAxisMaximum = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.BLOODSUGAR, Y_MAX_VALUE_DEFAULT);
                for (int datasetIndex = 0; datasetIndex < data.getScatterData().getDataSetCount(); datasetIndex++) {
                    IScatterDataSet dataSet = data.getScatterData().getDataSetByIndex(datasetIndex);
                    for (int entryIndex = 0; entryIndex < dataSet.getEntryCount(); entryIndex++) {
                        float entryValue = dataSet.getEntryForIndex(entryIndex).getY();
                        if (entryValue > yAxisMaximum) {
                            yAxisMaximum = entryValue;
                        }
                    }
                }

                getAxisLeft().setAxisMaximum(yAxisMaximum + Y_MAX_VALUE_OFFSET);
                invalidate();
            }
        }, day).execute();
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        this.setTag(choisirletimeUS.toDateString(day));
        update();
    }

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry entry, Highlight highlight) {
        if (entry.getData() != null && entry.getData() instanceof Entry) {
            EntryEditActivity.show(getContext(), (Entry) entry.getData());
        }
    }

    @Override
    public void onNothingSelected() {

    }
}