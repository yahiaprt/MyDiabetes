package com.mydiabetesprt.diabetes.future.Entrer.editor.measuration;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.future.Entrer.editor.EntryEditActivity;
import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;

import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditMeasurementPinTest {

    private static final String CATEGORY = "Blood Sugar";

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(EntryEditActivity.class);
        EntryEditMeasurementTestUtils.openFloatingMenuForCategories();
        Espresso.onView(ViewMatchers.withText(CATEGORY))
            .perform(ViewActions.click());
    }

    @Test
    public void clickingCheckBoxOnce_shouldShowSnackbarWithConfirmedPin() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.category_pin))
            .perform(ViewActions.click());
        Espresso.onView(AllOf.allOf(
            ViewMatchers.withId(com.google.android.material.R.id.snackbar_text),
            ViewMatchers.withText(CATEGORY + " has been pinned")
        )).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickingCheckBoxTwice_shouldShowSnackbarWithConfirmedUnpin() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.category_pin))
            .perform(ViewActions.click(), ViewActions.click());
        Espresso.onView(AllOf.allOf(
            ViewMatchers.withId(com.google.android.material.R.id.snackbar_text),
            ViewMatchers.withText(CATEGORY + " has been unpinned")
        )).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
