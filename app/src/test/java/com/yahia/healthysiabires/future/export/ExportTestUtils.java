package com.yahia.healthysiabires.future.export;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.future.export.job.FileType;

class ExportTestUtils {

    static void selectFileType(FileType fileType) {
        Espresso.onView(ViewMatchers.withId(R.id.format_spinner))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(fileType.extension.toUpperCase()))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());
    }
}
