package com.yahia.healthysiabires.future.timeline;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.yahia.healthysiabires.future.timeline.jour.tempslineDayFragment;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 02.08.2015.
 */
class tempslineViewPager extends ViewPager {

    interface Listener {
        void onDaySelected(DateTime day);
    }

    private tempslineViewPagerAdapter adapter;
    private OnPageChangeListener onPageChangeListener;
    private int scrollOffset;

    public tempslineViewPager(Context context) {
        super(context);
    }

    public tempslineViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setup(final FragmentManager fragmentManager, final Listener callback) {
        adapter = new tempslineViewPagerAdapter(
            fragmentManager,
            DateTime.now(),
                new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        scrollOffset = scrollY;
                    }
                }
        );
        setAdapter(adapter);
        setCurrentItem(adapter.getMiddle(), false);

        // Prevent destroying offscreen fragments that occur on fast scrolling
        setOffscreenPageLimit(2);

        if (onPageChangeListener == null) {
            onPageChangeListener = new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (position != adapter.getMiddle() && adapter.getItem(position) instanceof tempslineDayFragment) {
                        tempslineDayFragment fragment = (tempslineDayFragment) adapter.getItem(position);
                        fragment.scrollTo(scrollOffset);
                        callback.onDaySelected(fragment.getDay());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        int currentItem = getCurrentItem();
                        int targetItem = adapter.getMiddle();

                        ((tempslineDayFragment) adapter.getItem(currentItem)).update();

                        if (currentItem != targetItem) {
                            switch (currentItem) {
                                case 0:
                                    adapter.previousDay();
                                    break;
                                case 2:
                                    adapter.nextDay();
                                    break;
                            }
                            setCurrentItem(targetItem, false);
                        }
                    }
                }
            };
        } else {
            removeOnPageChangeListener(onPageChangeListener);
        }
        addOnPageChangeListener(onPageChangeListener);
    }

    void setDay(DateTime day) {
        adapter.setDay(day);
    }

    void reset() {
        adapter.reset();
    }
}
