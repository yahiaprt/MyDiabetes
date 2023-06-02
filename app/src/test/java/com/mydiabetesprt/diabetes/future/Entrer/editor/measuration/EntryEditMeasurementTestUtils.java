package com.mydiabetesprt.diabetes.future.Entrer.editor.measuration;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.type;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.AllOf;

public class EntryEditMeasurementTestUtils {

    public static void openFloatingMenuForCategories() {
        Espresso.onView(AllOf.allOf(
            ViewMatchers.withParent(ViewMatchers.withId(R.id.fab_menu)),
            ViewMatchers.withClassName(CoreMatchers.endsWith("ImageView")),
            ViewMatchers.isDisplayed())
        ).perform(ViewActions.click());
    }

    public static void openPickerForCategories() {
        Espresso.onView(ViewMatchers.withText("All"))
            .perform(ViewActions.click());
    }

    public static void selectCategoryFromPicker(type category) {
        Espresso.onView(ViewMatchers.withText(category.getStringResId()))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.scrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK"))
            .perform(ViewActions.click());
    }
}
