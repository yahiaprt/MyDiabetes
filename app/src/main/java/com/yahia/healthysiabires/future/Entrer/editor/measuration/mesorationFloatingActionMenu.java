package com.yahia.healthysiabires.future.Entrer.editor.measuration;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.view.floatingactionbutton.FloatingActionButtonusin;
import com.yahia.healthysiabires.partage.view.resource.ColorUs;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 30.09.2015.
 */
public class mesorationFloatingActionMenu extends FloatingActionMenu {

    private static final int MAX_BUTTON_COUNT = 3;

    private List<type> categoriesToSkip;
    private OnFabSelectedListener onFabSelectedListener;

    public mesorationFloatingActionMenu(Context context) {
        super(context);
        init();
    }

    public mesorationFloatingActionMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        categoriesToSkip = new ArrayList<>();
        enableCloseOnClickOutside();
    }

    private void enableCloseOnClickOutside() {
        setOnTouchListener((view, event) -> {
            if (isOpened()) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    close(true);
                }
                return true;
            } else {
                return false;
            }
        });
    }

    public void ignore(type category) {
        if (!categoriesToSkip.contains(category)) {
            categoriesToSkip.add(category);
        }
    }

    public void removeIgnore(type category) {
        if (categoriesToSkip.contains(category)) {
            categoriesToSkip.remove(category);
        }
    }

    private boolean hasChanged() {
        // TODO: Check whether ignores have changed
        return true;
    }

    public void restock() {
        if (hasChanged()) {
            removeAllMenuButtons();

            type[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
            int menuButtonCount = 0;
            int position = 0;
            while (position < activeCategories.length && menuButtonCount < MAX_BUTTON_COUNT) {
                type category = activeCategories[position];
                if (!categoriesToSkip.contains(category)) {
                    addMenuButton(category);
                    menuButtonCount++;
                }
                position++;
            }

            FloatingActionButton fabAll = FloatingActionButtonusin.createFloatingActionButton(
                getContext(),
                getContext().getString(R.string.all),
                R.drawable.ic_more,
                ColorUs.getBackgroundSecondary(getContext()));

            addMenuButton(fabAll);

            fabAll.setOnClickListener(v -> {
                close(true);
                if (hasMeasurementFloatingActionMenuCallback()) {
                    onFabSelectedListener.onMiscellaneousSelected();
                }
            });
        }
    }

    public void addMenuButton(final type category) {
        FloatingActionButton fab = FloatingActionButtonusin.createFloatingActionButton(
            getContext(),
            getContext().getString(category.getStringResId()),
            category.getIconImageResourceId(),
            ContextCompat.getColor(getContext(), R.color.green));
        fab.setOnClickListener(v -> {
            close(true);
            if (hasMeasurementFloatingActionMenuCallback()) {
                onFabSelectedListener.onCategorySelected(category);
            }
        });
        addMenuButton(fab);
    }

    public void setOnFabSelectedListener(OnFabSelectedListener onFabSelectedListener) {
        this.onFabSelectedListener = onFabSelectedListener;
    }

    private boolean hasMeasurementFloatingActionMenuCallback() {
        return onFabSelectedListener != null;
    }

    public interface OnFabSelectedListener {
        void onCategorySelected(type category);

        void onMiscellaneousSelected();
    }
}
