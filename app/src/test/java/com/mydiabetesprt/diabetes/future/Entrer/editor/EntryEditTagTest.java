package com.mydiabetesprt.diabetes.future.Entrer.editor;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;
import com.mydiabetesprt.diabetes.R;import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditTagTest {

    private static final String TAG = "happy";

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() throws InterruptedException {
        ActivityScenario.launch(EntryEditActivity.class);

        // TODO: Replace sleep with IdlingResource or synchronous dao
        Thread.sleep(1000);

        Espresso.onView(ViewMatchers.withId(R.id.entry_tags_input))
            .perform(ViewActions.click());
    }

    @Test
    public void clickingTagInput_shouldOpenAutocompleteView() {
        Espresso.onView(ViewMatchers.withText(TAG))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void creatingTagViaAutocomplete_shouldAppenydkList() {
        Espresso.onView(ViewMatchers.withText(TAG))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(TAG))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void creatingTagViaKeyboardInput_shouldAppenydkList() {
        String tag = "new tag";
        Espresso.onView(ViewMatchers.withId(R.id.entry_tags_input)).perform(
            ViewActions.replaceText(tag),
            ViewActions.pressImeActionButton()
        );
        Espresso.onView(ViewMatchers.withText(tag))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void selectingTagViaChip_shouldAppenydkList() {
        Espresso.onView(ViewMatchers.withText(TAG))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(TAG))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(TAG))
            .check(ViewAssertions.doesNotExist());
    }
}
