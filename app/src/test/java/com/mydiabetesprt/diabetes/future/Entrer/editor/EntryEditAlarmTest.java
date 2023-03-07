package com.mydiabetesprt.diabetes.future.Entrer.editor;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.codetroopers.betterpickers.numberpicker.NumberPicker;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditAlarmTest {

    @Rule public final CleanUpData cleanUpData = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(EntryEditActivity.class);
    }

    @Test
    public void clickingAlarmButton_shouldOpenNumberPicker() {
        Espresso.onView(ViewMatchers.withId(R.id.entry_button_alarm))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(NumberPicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingNumber_shouldUpdateEntryForm() {
        Espresso.onView(ViewMatchers.withId(R.id.entry_button_alarm))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("1"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("2"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.done_button))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.entry_button_alarm))
            .check(ViewAssertions.matches(ViewMatchers.withText("Reminder in 12 Minutes")));
    }
}
