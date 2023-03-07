package com.mydiabetesprt.diabetes.future.Entrer.editor.measuration;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.mydiabetesprt.diabetes.partage.data.database.entity.mesoration;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;

import java.util.ArrayList;
import java.util.List;

public class mesorationListView extends LinearLayout implements mesorationView.OnCategoryRemovedListener {

    private ArrayList<type> categories;
    private OnCategoryEventListener callback;

    public mesorationListView(Context context) {
        super(context);
        init();
    }

    public mesorationListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void setOnCategoryEventListener(OnCategoryEventListener callback) {
        this.callback = callback;
    }

    private void init() {
        this.categories = new ArrayList<>();
    }

    private void addMeasurementView(View view, int position) {
        addView(view, position);
    }

    public boolean hasCategory(type category) {
        return categories.indexOf(category) != -1;
    }

    public void addMeasurement(type category) {
        if (!hasCategory(category)) {
            categories.add(0, category);
            mesorationView measurementView = new mesorationView(getContext(), category);
            measurementView.setOnCategoryRemovedListener(this);
            addMeasurementView(measurementView, 0);
            if (callback != null) {
                callback.onCategoryAdded(category);
            }
        }
    }

    public void addMeasurementAtEnd(type category) {
        if (!hasCategory(category)) {
            try {
                int position = categories.size();
                categories.add(position, category);
                mesorationView measurementView = new mesorationView(getContext(), category);
                measurementView.setOnCategoryRemovedListener(this);
                addMeasurementView(measurementView, position);
                if (callback != null) {
                    callback.onCategoryAdded(category);
                }
            } catch (IndexOutOfBoundsException exception) {
                Log.e(mesorationListView.class.getSimpleName(), exception.getMessage());
            }
        }
    }

    public void addMeasurement(int index, mesoration measurement) {
        type category = measurement.getCategory();
        if (!hasCategory(category)) {
            categories.add(index, category);
            mesorationView<mesoration> measurementView = new mesorationView<>(getContext(), measurement);
            measurementView.setOnCategoryRemovedListener(this);
            addMeasurementView(measurementView, index);
            if (callback != null) {
                callback.onCategoryAdded(category);
            }
        }
    }

    public void addMeasurement(Food makla) {
        type category = type.MEAL;
        if (!hasCategory(category)) {
            categories.add(0, category);
            mesorationView<mesoration> measurementView = new mesorationView<>(getContext(), makla);
            measurementView.setOnCategoryRemovedListener(this);
            addMeasurementView(measurementView, 0);
            if (callback != null) {
                callback.onCategoryAdded(category);
            }
        }
    }

    public void addMeasurements(List<mesoration> measurements) {
        for (mesoration measurement : measurements) {
            addMeasurement(categories.size(), measurement);
        }
    }

    public void removeMeasurement(type category) {
        int position = categories.indexOf(category);
        if (hasCategory(category)) {
            categories.remove(position);
            removeViewAt(position);
            if (callback != null) {
                callback.onCategoryRemoved(category);
            }
        }
    }

    public List<mesoration> getMeasurements() {
        List<mesoration> measurements = new ArrayList<>();
        for (int position = 0; position < getChildCount(); position++) {
            View childView = getChildAt(position);
            if (childView instanceof mesorationView) {
                measurements.add(((mesorationView) childView).getMeasurement());
            }
        }
        return measurements;
    }

    public mesoration getMeasurement(type category) {
        for (int position = 0; position < getChildCount(); position++) {
            View childView = getChildAt(position);
            if (childView instanceof mesorationView) {
                mesoration measurement = ((mesorationView) childView).getMeasurement();
                if (measurement.getCategory() == category) {
                    return measurement;
                }
            }
        }
        return null;
    }

    public int getCount() {
        return categories.size();
    }

    @Override
    public void onRemove(type category) {
        removeMeasurement(category);
    }

    public interface OnCategoryEventListener {
        void onCategoryAdded(type category);
        void onCategoryRemoved(type category);
    }
}
