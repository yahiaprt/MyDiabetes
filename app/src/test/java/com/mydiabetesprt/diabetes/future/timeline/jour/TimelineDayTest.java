package com.mydiabetesprt.diabetes.future.timeline.jour;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.test.junit.rule.ApplyAppTheme;
import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TimelineDayTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final CleanUpData cleanUpData = new CleanUpData();

    @Before
    public void setup() {
        FragmentScenario.launchInContainer(tempslineDayFragment.class);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
    }

    @Test
    public void showsChart() {
        Espresso.onView(ViewMatchers.withId(R.id.day_chart))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void showsTable() {
        Espresso.onView(ViewMatchers.withId(R.id.category_table_images))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.onView(ViewMatchers.withId(R.id.category_table_values))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
