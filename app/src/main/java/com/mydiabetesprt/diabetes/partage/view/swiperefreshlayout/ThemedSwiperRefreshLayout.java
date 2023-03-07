package com.mydiabetesprt.diabetes.partage.view.swiperefreshlayout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mydiabetesprt.diabetes.partage.view.resource.ColorUs;

public class ThemedSwiperRefreshLayout extends SwipeRefreshLayout {

    public ThemedSwiperRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ThemedSwiperRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setProgressBackgroundColorSchemeColor(ColorUs.getBackgroundSecondary(getContext()));
    }
}
