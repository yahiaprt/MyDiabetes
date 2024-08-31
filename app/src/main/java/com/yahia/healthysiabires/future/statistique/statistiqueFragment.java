package com.yahia.healthysiabires.future.statistique;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

   
import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.database.ydk.mesorationydk;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.partage.view.resource.ColorUs;
import com.yahia.healthysiabires.partage.view.fragment.BaseFragment;
import com.yahia.healthysiabires.future.datetemps.letempsSpan;
import com.yahia.healthysiabires.partage.view.chart.ChartUs;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class statistiqueFragment extends BaseFragment {
    Boolean y=true;
     
    private static final int MIN_MAX_Y_VALUE = 3;

    @BindView(R.id.statistics_image_category) ImageView imageViewCategory;
    @BindView(R.id.statistics_categories) Spinner spinnerCategories;
    @BindView(R.id.statistics_interval) Spinner spinnerInterval;
    @BindView(R.id.statistics_measurement_count_avg) TextView textViewMeasurementCountAvg;
    @BindView(R.id.statistics_layout_hyper) ViewGroup layoutAvgHyper;
    @BindView(R.id.statistics_avg_hyper) TextView textViewAvgHyper;
    @BindView(R.id.statistics_layout_hypo) ViewGroup layoutAvgHypo;
    @BindView(R.id.statistics_avg_hypo) TextView textViewAvgHypo;
    @BindView(R.id.statistics_avg_unit) TextView textViewAvgUnit;
    @BindView(R.id.statistics_avg_value) TextView textViewAvgValue;
    @BindView(R.id.statistics_chart_trend) LineChart chartTrend;
    @BindView(R.id.layout_distribution) ViewGroup layoutDistribution;
    @BindView(R.id.statistics_chart_distribution) PieChart chartDistribution;

    private letempsSpan timeSpan;
    private type category;

    public statistiqueFragment() {
        super(R.layout.fragment_statistic, R.string.statistics);


       //        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//         mInterstitialAd = new InterstitialAd(getContext());
//        mInterstitialAd.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                finish();
//            }
//
//        });
//
//        }
//
//    @Override
//    public void onBackPressed() {
//        showIntewrestial();
//
//    }
//    @Override
//    public void onBackButtonPressed() {
//        if (y=true) {
//            showIntewrestial();
//        } else {
//            getActivity().onBackPressed();
//        }
//    }
//
//    public  void showIntewrestial(){
//        if(mInterstitialAd.isLoaded()){
//            mInterstitialAd.show();
//        }else {finish();
//        }

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

  

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void init() {
        this.timeSpan = letempsSpan.WEEK;
        this.category = type.BLOODSUGAR;
        initLayout();
    }

    private void initLayout() {
        initializeCharts();
        initSpinners();
    }

    private void initSpinners() {
        final type[] categories = PreferenceHelper.getInstance().getActiveCategories();
        List<String> categoryNames = new ArrayList<>();
        for (type category : categories) {
            categoryNames.add(getString(category.getStringResId()));
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeCategory(categories[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final letempsSpan[] timeSpans = letempsSpan.values();
        List<String> timeSpanNames = new ArrayList<>();
        for (letempsSpan timeSpan : timeSpans) {
            timeSpanNames.add(timeSpan.toIntervalLabel(getContext()));
        }
        ArrayAdapter<String> timeSpanAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeSpanNames);
        timeSpanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterval.setAdapter(timeSpanAdapter);
        spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTimeInterval(timeSpans[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateContent() {
        updateViews();
        updateCharts();
    }

    private void changeTimeInterval(letempsSpan timeSpan) {
        this.timeSpan = timeSpan;
        updateContent();
    }

    private void changeCategory(type category) {
        this.category = category;
        updateContent();
    }

    private void updateViews() {
        Interval interval = timeSpan.getInterval(DateTime.now(), -1);
        long days = interval.toDuration().getStandardDays();

        imageViewCategory.setImageResource(category.getIconImageResourceId());

        mesoration avgMeasurement = mesorationydk.getInstance(category.toClass()).getAvgMeasurement(category, interval);
        textViewAvgUnit.setText(category.stackValues() ?
                String.format("%s %s", PreferenceHelper.getInstance().getUnitName(category), getString(R.string.per_day)) :
                PreferenceHelper.getInstance().getUnitName(category));

        textViewAvgValue.setText(avgMeasurement.toString());

        long count = EntryDao.getInstance().count(category, interval.getStart(), interval.getEnd());
        float avgCountPerDay = (float) count / (float) days;
        textViewMeasurementCountAvg.setText(FloatUs.parseFloat(avgCountPerDay));

        if (category == type.BLOODSUGAR) {
            layoutAvgHyper.setVisibility(View.VISIBLE);
            layoutAvgHypo.setVisibility(View.VISIBLE);
            long hyperCount = EntryDao.getInstance().countAbove(interval.getStart(), interval.getEnd(), PreferenceHelper.getInstance().getLimitHyperglycemia());
            long hypoCount = EntryDao.getInstance().countBelow(interval.getStart(), interval.getEnd(), PreferenceHelper.getInstance().getLimitHypoglycemia());
            float avgHypersPerDay = (float) hyperCount / (float) days;
            float avgHyposPerDay = (float) hypoCount / (float) days;
            textViewAvgHyper.setText(FloatUs.parseFloat(avgHypersPerDay));
            textViewAvgHypo.setText(FloatUs.parseFloat(avgHyposPerDay));
        } else {
            layoutAvgHyper.setVisibility(View.GONE);
            layoutAvgHypo.setVisibility(View.GONE);
        }
    }

    // region Charting

    private void initializeCharts() {
        ChartUs.setChartDefaultStyle(chartTrend, category);

        @ColorInt int textColor = ColorUs.getTextColorSecondary(getContext());
        chartTrend.getAxisLeft().setTextColor(textColor);
        chartTrend.getXAxis().setDrawAxisLine(true);
        chartTrend.getAxisLeft().setDrawAxisLine(false);
        chartTrend.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartTrend.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chartTrend.getXAxis().setTextColor(textColor);
        chartTrend.getXAxis().setValueFormatter((value, axis) -> {
            int daysPast = -(timeSpan.stepsPerInterval - (int) value);
            DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
            return timeSpan.getLabel(dateTime);
        });
        chartTrend.setTouchEnabled(false);

        chartDistribution.setDrawHoleEnabled(false);
        chartDistribution.setUsePercentValues(true);
        chartDistribution.setDescription(null);
        chartDistribution.setDrawEntryLabels(false);
        chartDistribution.setNoDataText(getString(ChartUs.NO_DATA_TEXT_RESOURCE_ID));
        chartDistribution.getPaint(Chart.PAINT_INFO).setColor(ContextCompat.getColor(getContext(), ChartUs.NO_DATA_COLOR_RESOURCE_ID));
        chartDistribution.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartDistribution.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chartDistribution.getLegend().setTextColor(ColorUs.getTextColorPrimary(getContext()));
    }


    private void updateCharts() {
        new MeasurationAverageTask(getContext(), category, timeSpan, false, true, lineData -> {
            if (isAdded()) {
                boolean hasData = false;

                if (lineData != null) {
                    for (ILineDataSet lineDataSet : lineData.getDataSets()) {
                        if (lineDataSet.getEntryCount() > 0) {
                            hasData = true;
                            break;
                        }
                    }
                }

                chartTrend.getAxisLeft().removeAllLimitLines();

                ViewGroup.LayoutParams params = chartTrend.getLayoutParams();
                params.height = hasData ? (int) getResources().getDimension(R.dimen.line_chart_height_detailed) : ViewGroup.LayoutParams.WRAP_CONTENT;
                chartTrend.setLayoutParams(params);

                if (hasData) {
                    float yAxisMinValue = PreferenceHelper.getInstance().getExtrema(category)[0] * .9f;
                    float yAxisMinCustomValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, yAxisMinValue);
                    chartTrend.getAxisLeft().setAxisMinValue(yAxisMinCustomValue);

                    float yAxisMaxCustomValue = lineData.getYMax();
                    yAxisMaxCustomValue = yAxisMaxCustomValue > MIN_MAX_Y_VALUE ? yAxisMaxCustomValue : MIN_MAX_Y_VALUE;
                    chartTrend.getAxisLeft().setAxisMaxValue(yAxisMaxCustomValue * 1.1f);

                    if (category == type.BLOODSUGAR) {
                        float targetValue = PreferenceHelper.getInstance().
                                formatDefaultToCustomUnit(type.BLOODSUGAR,
                                        PreferenceHelper.getInstance().getTargetValue());
                        chartTrend.getAxisLeft().addLimitLine(ChartUs.getLimitLine(getContext(), targetValue, R.color.green));

                        if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                            float limitHypo = PreferenceHelper.getInstance().
                                    formatDefaultToCustomUnit(type.BLOODSUGAR,
                                            PreferenceHelper.getInstance().getLimitHypoglycemia());
                            chartTrend.getAxisLeft().addLimitLine(ChartUs.getLimitLine(getContext(), limitHypo, R.color.blue));
                            float limitHyper = PreferenceHelper.getInstance().
                                    formatDefaultToCustomUnit(type.BLOODSUGAR,
                                            PreferenceHelper.getInstance().getLimitHyperglycemia());
                            chartTrend.getAxisLeft().addLimitLine(ChartUs.getLimitLine(getContext(), limitHyper, R.color.red));
                        }
                    }

                    chartTrend.setData(lineData);
                    chartTrend.getXAxis().setAxisMinimum(0);
                    chartTrend.getXAxis().setAxisMaximum(timeSpan.stepsPerInterval);
                    chartTrend.invalidate();
                } else {
                    chartTrend.clear();
                }
            }
        }).execute();

        if (category == type.BLOODSUGAR) {
            layoutDistribution.setVisibility(View.VISIBLE);

            new glucimieDistributionTask(getContext(), timeSpan, pieData -> {
                if (isAdded()) {
                    boolean hasData = pieData.getDataSet().getEntryCount() > 0;
                    chartDistribution.setData(hasData ? pieData : null);
                    ViewGroup.LayoutParams params = chartDistribution.getLayoutParams();
                    params.height = hasData ? (int) getResources().getDimension(R.dimen.pie_chart_height) : ViewGroup.LayoutParams.WRAP_CONTENT;
                    chartDistribution.setLayoutParams(params);
                    chartDistribution.invalidate();
                }
            }).execute();
        } else {
            layoutDistribution.setVisibility(View.GONE);
        }
    }

    // endregion
}