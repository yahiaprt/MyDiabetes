package com.yahia.healthysiabires.future.Entrer.editor.measuration;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;

import java.lang.reflect.Constructor;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public abstract class mesorationAbstractView<T extends mesoration> extends LinearLayout {

    private static final String TAG = mesorationAbstractView.class.getSimpleName();

    protected T measurement;
    protected Food makla;

    @Deprecated
    public mesorationAbstractView(Context context) {
        super(context);
        init();
    }

    @Deprecated
    public mesorationAbstractView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public mesorationAbstractView(Context context, T measurement) {
        super(context);
        this.measurement = measurement;
        init();
    }

    public mesorationAbstractView(Context context, Food makla) {
        super(context);
        this.measurement = (T) new Meal();
        this.makla = makla;
        init();
    }

    public mesorationAbstractView(Context context, type category) {
        super(context);
        try {
            Class<T> clazz = category.toClass();
            Constructor<T> constructor = clazz.getConstructor();
            measurement = constructor.newInstance();
        } catch (Exception exception) {
            Log.e(TAG, String.format("Could not get newInstance for %s", category.toClass().getSimpleName()));
        }
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initLayout();
        if (measurement != null) {
            setValues();
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutResourceId();

    protected abstract void initLayout();

    protected abstract void setValues();

    protected abstract boolean isValid();

    public abstract mesoration getMeasurement();
}