package com.mydiabetesprt.diabetes.future.Entrer.editor.measuration;

import android.content.Context;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.entity.mesoration;
import com.mydiabetesprt.diabetes.partage.data.database.entity.tension;
import com.mydiabetesprt.diabetes.partage.view.texteditor.StickHintInput;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class tensionPressureView extends mesorationAbstractView<tension> {

    @BindView(R.id.pressure_systolic)
    StickHintInput systolic;
    @BindView(R.id.pressure_diastolic)
    StickHintInput diastolic;

    public tensionPressureView(Context context) {
        super(context, type.PRESSURE);
    }

    public tensionPressureView(Context context, tension pressure) {
        super(context, pressure);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_pressure;
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void setValues() {
        systolic.setText(measurement.getValuesForUI()[0]);
        diastolic.setText(measurement.getValuesForUI()[1]);
    }

    @Override
    protected boolean isValid() {
        return PreferenceHelper.getInstance().isValueValid(systolic.getInputView(), type.PRESSURE) &&
                PreferenceHelper.getInstance().isValueValid(diastolic.getInputView(), type.PRESSURE);
    }

    @Override
    public mesoration getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            FloatUs.parseNumber(systolic.getText())),
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            measurement.getCategory(),
                            FloatUs.parseNumber(diastolic.getText())));
            return measurement;
        } else {
            return null;
        }
    }
}