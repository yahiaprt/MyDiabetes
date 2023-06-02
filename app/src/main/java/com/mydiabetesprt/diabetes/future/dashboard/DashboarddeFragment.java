package com.mydiabetesprt.diabetes.future.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mydiabetesprt.diabetes.future.Entrer.editor.EntryEditActivity;
import com.mydiabetesprt.diabetes.R;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.mesorationydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.glycemie;
import com.mydiabetesprt.diabetes.partage.event.data.EntreAddedEt;
import com.mydiabetesprt.diabetes.partage.event.preference.UnitChangedEt;
import com.mydiabetesprt.diabetes.future.datetemps.choisirletimeUS;
import com.mydiabetesprt.diabetes.future.datetemps.letempsSpan;
import com.mydiabetesprt.diabetes.future.navigation.MainActivity;
import com.mydiabetesprt.diabetes.partage.view.fragment.BaseFragment;
import com.mydiabetesprt.diabetes.future.navigation.MainButton;
import com.mydiabetesprt.diabetes.future.navigation.MainButtonProperties;
import com.mydiabetesprt.diabetes.partage.view.chart.ChartUs;
import com.mydiabetesprt.diabetes.future.alarm.AlarmUS;
import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;
import com.mydiabetesprt.diabetes.future.statistique.MeasurationAverageTask;
import com.github.mikephil.charting.charts.LineChart;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Minutes;

import butterknife.BindView;
import butterknife.OnClick;

public class DashboarddeFragment extends BaseFragment implements MainButton {
    Boolean y = true;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;

    @BindView(R.id.layout_alarm)
    ViewGroup layoutAlarm;
    @BindView(R.id.alarm_text)
    TextView textViewAlarm;
    @BindView(R.id.alarm_delete)
    View buttonAlarmDelete;
    @BindView(R.id.textview_latest_value)
    TextView textViewLatestValue;
    @BindView(R.id.textview_latest_time)
    TextView textViewLatestTime;
    @BindView(R.id.textview_latest_ago)
    TextView textViewLatestAgo;
    @BindView(R.id.textview_hyperglycemia)
    TextView textViewHyperglycemia;
    @BindView(R.id.textview_hypoglycemia)
    TextView textViewHypoglycemia;
    @BindView(R.id.hba1c_value)
    TextView textViewHbA1c;
    private Entry latestEntry;
    private Context mContext;

    public DashboarddeFragment() {
        super(R.layout.fragment_dashboard, R.string.app_name, R.menu.dashboard);
//

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //  finish();
            }

        });


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd2 = new InterstitialAd(getContext());
        mInterstitialAd2.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
        mInterstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //     finish();
            }

        });


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd3 = new InterstitialAd(getContext());
        mInterstitialAd3.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd3.loadAd(new AdRequest.Builder().build());
        mInterstitialAd3.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //   finish();
            }

        });
//
//

        //initializeChart();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onBackButtonPressed() {

    }


    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void updateContent() {
        latestEntry = EntryDao.getInstance().getLatestWithMeasurement(glycemie.class);
        updateReminder();
        updateLatest();
        updateDashboard();
     //   updateChart();
    }

    private void updateReminder() {
        if (AlarmUS.isAlarmSet()) {
            final long alarmIntervalInMillis = AlarmUS.getAlarmInMillis() - DateTime.now().getMillis();

            layoutAlarm.setVisibility(View.VISIBLE);
            textViewAlarm.setText(String.format("%s %s",
                    getString(R.string.alarm_reminder_in),
                    choisirletimeUS.parseInterval(getContext(), alarmIntervalInMillis)));

            buttonAlarmDelete.setOnClickListener(v -> {
                AlarmUS.stopAlarm();
                updateReminder();


                ViewUs.showSnackbar(getView(), getString(R.string.alarm_deleted), v1 -> {
                    AlarmUS.setAlarm(alarmIntervalInMillis);
                    updateReminder();
                });
            });
        } else {
            layoutAlarm.setVisibility(View.GONE);
        }
    }

    private void updateLatest() {
        if (getContext() != null) {
            if (latestEntry != null) {
                textViewLatestValue.setTextSize(54);
                glycemie bloodSugar = (glycemie) mesorationydk.getInstance(glycemie.class).getMeasurement(latestEntry);
                textViewLatestValue.setBackground(ContextCompat.getDrawable(getContext(), R.color.transparent));
                textViewLatestValue.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                textViewLatestValue.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;

                // Value
                textViewLatestValue.setText(bloodSugar.toString());

                // Highlighting
                if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                    if (bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    } else if (bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                    } else {
                        textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    }
                }

                // Time
                textViewLatestTime.setText(String.format("%s %s - ",
                        Helper.getDateFormat().print(latestEntry.getDate()),
                        Helper.getTimeFormat().print(latestEntry.getDate())));
                int differenceInMinutes = Minutes.minutesBetween(latestEntry.getDate(), new DateTime()).getMinutes();

                // Highlight if last measurement is more than eight hours ago
                textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.bpBlue));
                if (differenceInMinutes > DateTimeConstants.MINUTES_PER_HOUR * 8) {
                    textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                }

                textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
            } else {
                textViewLatestValue.setTextSize(32);
                textViewLatestValue.setText(R.string.first_visit);
                textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                textViewLatestTime.setText(R.string.first_visit_desc);
                textViewLatestAgo.setText(null);
                textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_darker));
            }
        }
    }

    private void updateDashboard() {
        new DashboarddeTask(getContext(), values -> {
            if (isAdded() && values != null && values.length == 7) {
                textViewHyperglycemia.setText(values[1]);
                textViewHypoglycemia.setText(values[2]);
                textViewHbA1c.setText(values[6]);
            }
        }).execute();
    }


//    private void initializeChart() {
//        final letempsSpan timeSpan = letempsSpan.WEEK;
//        ChartUs.setChartDefaultStyle(chart, type.BLOODSUGAR);
//        chart.setTouchEnabled(false);
//        chart.getAxisLeft().setDrawAxisLine(false);
//        chart.getAxisLeft().setDrawGridLines(false);
//        chart.getAxisLeft().setDrawLabels(false);
//        chart.getXAxis().setDrawGridLines(false);
//        chart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
//        chart.getAxisLeft().removeAllLimitLines();
//        float targetValue = PreferenceHelper.getInstance().
//                formatDefaultToCustomUnit(type.BLOODSUGAR,
//                        PreferenceHelper.getInstance().getTargetValue());
//        chart.getAxisLeft().addLimitLine(ChartUs.getLimitLine(getContext(), targetValue, R.color.gray_light));
//        chart.getXAxis().setValueFormatter((value, axis) -> {
//            int daysPast = -(timeSpan.stepsPerInterval - (int) value);
//            DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
//            return timeSpan.getLabel(dateTime);
//        });
//        chart.getXAxis().setAxisMaximum(timeSpan.stepsPerInterval);
//    }

    public void showIntewrestial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void showIntewrestial2() {
        if (mInterstitialAd2.isLoaded()) {
            mInterstitialAd2.show();
        }
    }


//
    public void showIntewrestial3() {
        if (mInterstitialAd3.isLoaded()) {
            mInterstitialAd3.show();
        }
    }
//

//    private void updateChart() {
//        new MeasurationAverageTask(getContext(), type.BLOODSUGAR, letempsSpan.WEEK, true, false, lineData -> {
//            if (isAdded()) {
//                chart.clear();
//                if (lineData != null) {
//                    chart.setData(lineData);
//                }
//                chart.invalidate();
//            }
//        }).execute();
//    }

    private void openStatistics() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(R.id.nav_statistics);
            //  showIntewrestial3();
        }
    }

    private void openMeal() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(R.id.nav_makla_database);
            //  showIntewrestial3();
        }
    }

    private void openCalculator() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(R.id.nav_calculator);
            //  showIntewrestial3();
        }
    }

    private void openExport() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(R.id.nav_export);
            //  showIntewrestial3();
        }
    }

    private void openSettings() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(R.id.nav_settings);
            //  showIntewrestial3();
        }
    }

    private void openTimeline() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(R.id.nav_timeline);
            //  showIntewrestial3();
        }
    }

       @OnClick(R.id.layout_latest)
    protected void openEntry(View view) {
        EntryEditActivity.show(getContext(), latestEntry);
        showIntewrestial();
        showIntewrestial2();

    }

    @OnClick(R.id.layout_today)
    protected void openStatisticsToday() {
        showIntewrestial();
        showIntewrestial2();
        openStatistics();

    }




    @OnClick(R.id.layout_hba1c)
    protected void showHbA1cFormula() {
        String formula = String.format(getString(R.string.hba1c_formula),
                getString(R.string.average_symbol),
                getString(R.string.months),
                getString(R.string.bloodsugar));
        ViewUs.showSnackbar(getView(), formula);
        showIntewrestial();

    }

    @OnClick(R.id.layout_settings)
    protected void settings() {

        openSettings();
        showIntewrestial();
        showIntewrestial2();
    }

    @OnClick(R.id.layout_calculation)
    protected void calculation() {

        openCalculator();
        showIntewrestial();
        showIntewrestial2();
    }

    @OnClick(R.id.layout_export)
    protected void export() {

        openExport();
        showIntewrestial();
        showIntewrestial2();
    }

    @OnClick(R.id.layout_timeline)
    protected void timeline() {

        openTimeline();
        showIntewrestial();
        showIntewrestial2();
    }

    @OnClick(R.id.layout_food)
    protected void food() {

        openMeal();
        showIntewrestial();
        showIntewrestial2();
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(view -> {
            if (getContext() != null) {
                EntryEditActivity.show(getContext());
            }
        }, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntreAddedEt event) {
        updateContent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEt event) {
        if (isAdded()) {
            updateContent();
        }
    }
}
