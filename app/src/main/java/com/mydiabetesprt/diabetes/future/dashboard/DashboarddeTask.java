package com.mydiabetesprt.diabetes.future.dashboard;

import android.content.Context;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.SqlFunction;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.mesorationydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.glycemie;
import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.BasesynchronisationTask;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.List;

/**
 * Created by Faltenreich on 05.11.2017
 */

public class DashboarddeTask extends BasesynchronisationTask<Void, Void, String[]> {

    public DashboarddeTask(Context context, OnAsyncProgressListener<String[]> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
    }

    protected String[] doInBackground(Void... params) {
        int countHypers = 0;
        int countHypos = 0;

        List<Entry> entriesWithBloodSugar = EntryDao.getInstance().getAllWithMeasurementFromToday(glycemie.class);
        if (entriesWithBloodSugar != null) {
            for (Entry entry : entriesWithBloodSugar) {
                glycemie bloodSugar = (glycemie) mesorationydk.getInstance(glycemie.class).getMeasurement(entry);
                if (bloodSugar != null) {
                    float mgDl = bloodSugar.getMgDl();
                    if (mgDl > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        countHypers++;
                    } else if (mgDl < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        countHypos++;
                    }
                }
            }
        }
        DateTime now = DateTime.now();
        Interval intervalDay = new Interval(now, now);
        Interval intervalWeek = new Interval(new DateTime(now.minusWeeks(1)), now);
        Interval intervalMonth = new Interval(new DateTime(now.minusMonths(1)), now);
        Interval intervalQuarter = new Interval(new DateTime(now.minusMonths(3)), now);

        float avgDay = mesorationydk.getInstance(glycemie.class).function(SqlFunction.AVG, glycemie.Column.MGDL, intervalDay);
        float avgDayCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.BLOODSUGAR, avgDay);

        float avgWeek = mesorationydk.getInstance(glycemie.class).function(SqlFunction.AVG, glycemie.Column.MGDL, intervalWeek);
        float avgWeekCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.BLOODSUGAR, avgWeek);

        float avgMonth = mesorationydk.getInstance(glycemie.class).function(SqlFunction.AVG, glycemie.Column.MGDL, intervalMonth);
        float avgMonthCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.BLOODSUGAR, avgMonth);

        float avgQuarter = mesorationydk.getInstance(glycemie.class).function(SqlFunction.AVG, glycemie.Column.MGDL, intervalQuarter);
        float hbA1cCustom = 0;
        if (avgQuarter > 0) {
            float hbA1c = Helper.calculateHbA1c(avgQuarter);
            hbA1cCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.HBA1C, hbA1c);
        }

        return new String[] {
                Integer.toString(entriesWithBloodSugar != null ? entriesWithBloodSugar.size() : 0),
                Integer.toString(countHypers),
                Integer.toString(countHypos),
                avgDayCustom > 0 ? FloatUs.parseFloat(avgDayCustom) : getContext().getString(R.string.placeholder),
                avgWeekCustom > 0 ? FloatUs.parseFloat(avgWeekCustom) : getContext().getString(R.string.placeholder),
                avgMonthCustom > 0 ? FloatUs.parseFloat(avgMonthCustom) : getContext().getString(R.string.placeholder),
                hbA1cCustom > 0 ? String.format("%s %s", FloatUs.parseFloat(hbA1cCustom), PreferenceHelper.getInstance().getUnitAcronym(type.HBA1C)) : getContext().getString(R.string.placeholder)
        };
    }
}
