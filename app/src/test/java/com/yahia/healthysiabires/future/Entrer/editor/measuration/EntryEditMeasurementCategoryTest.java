package com.yahia.healthysiabires.future.Entrer.editor.measuration;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.yahia.healthysiabires.future.Entrer.editor.EntryEditActivity;
import com.yahia.healthysiabires.test.junit.rule.CleanUpData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditMeasurementCategoryTest {

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(EntryEditActivity.class);
        EntryEditMeasurementTestUtils.openFloatingMenuForCategories();
    }

    @Test
    public void clickingFloatingActionMenu_shouldOpenMenu() {
        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Insulin"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Meal"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("All"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void selectingCategoryViaFloatingActionMenu_shouldAppenydkMeasurementList() {
        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void selectingAllButtonViaFloatingActionMenu_shouldOpenPickerDialog() {
        Espresso.onView(ViewMatchers.withText("All"))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Categories"))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void selectingMultipleCategoriesViaPicker_shouldAppenydkMeasurementList() {
        EntryEditMeasurementTestUtils.openPickerForCategories();

        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Insulin"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("OK"))
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Insulin"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
