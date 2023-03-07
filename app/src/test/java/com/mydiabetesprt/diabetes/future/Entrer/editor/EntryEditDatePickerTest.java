package com.mydiabetesprt.diabetes.future.Entrer.editor;

import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.Helper;

import org.hamcrest.Matchers;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditDatePickerTest {

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(EntryEditActivity.class);
    }

    @Test
    public void clickingDateButton_shouldOpenDatePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.button_date))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingDate_shouldApplyToForm() {
        LocalDate date = LocalDate.now().minusDays(1);
        Espresso.onView(ViewMatchers.withId(R.id.button_date))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .perform(PickerActions.setDate(date.year().get(), date.getMonthOfYear(), date.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_date))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(date))));
    }
}
