package com.yahia.healthysiabires.future.navigation;

import androidx.fragment.app.Fragment;

import com.yahia.healthysiabires.future.dashboard.DashboarddeFragment;
import com.yahia.healthysiabires.future.timeline.tempslineFragment;
import com.yahia.healthysiabires.future.log.LogFragment;
import com.yahia.healthysiabires.partage.view.fragment.BaseFragment;
import com.yahia.healthysiabires.future.export.ExportFragment;
import com.yahia.healthysiabires.future.statistique.statistiqueFragment;

public enum MainFragmentType {
    HOME(DashboarddeFragment.class, 0),
    TIMELINE(tempslineFragment.class, 1),
    LOG(LogFragment.class, 2),
    STATISTICS(statistiqueFragment.class, 5),
    EXPORT(ExportFragment.class, 6);

    public Class<? extends BaseFragment> fragmentClass;
    public int position;

    MainFragmentType(Class<? extends BaseFragment> fragmentClass, int position) {
        this.fragmentClass = fragmentClass;
        this.position = position;
    }

    public static MainFragmentType valueOf(Class<? extends Fragment> fragmentClass) {
        for (MainFragmentType mainFragmentType : MainFragmentType.values()) {
            if (mainFragmentType.fragmentClass == fragmentClass) {
                return mainFragmentType;
            }
        }
        return null;
    }

    public static MainFragmentType valueOf(int position) {
        for (MainFragmentType mainFragmentType : MainFragmentType.values()) {
            if (mainFragmentType.position == position) {
                return mainFragmentType;
            }
        }
        return null;
    }
}