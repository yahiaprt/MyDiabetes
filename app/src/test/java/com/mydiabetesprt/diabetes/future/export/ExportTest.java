package com.mydiabetesprt.diabetes.future.export;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.test.junit.rule.ApplyAppTheme;
import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class ExportTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        FragmentScenario.launchInContainer(ExportFragment.class);
    }

    @Test
    public void onStart_shouldStartAtStartOfWeek() {
        String date = Helper.getDateFormat().print(DateTime.now().withDayOfWeek(1));
        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(date)));
    }

    @Test
    public void onStart_shouldEndAtToday() {
        String date = Helper.getDateFormat().print(DateTime.now());
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(date)));
    }

    @Test
    public void onStart_shouldSelectPdf() {
        Espresso.onView(ViewMatchers.withId(R.id.format_spinner))
            .check(ViewAssertions.matches(ViewMatchers.withSpinnerText("PDF")));
    }
}
