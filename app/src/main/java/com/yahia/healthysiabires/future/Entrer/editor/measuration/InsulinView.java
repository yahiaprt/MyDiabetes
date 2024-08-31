package com.yahia.healthysiabires.future.Entrer.editor.measuration;

import android.content.Context;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.entity.Insulin;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.view.texteditor.StickHintInput;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.partage.data.premier.StringUs;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class InsulinView extends mesorationAbstractView<Insulin> {

    @BindView(R.id.insulin_bolus)
    StickHintInput inputBolus;
    @BindView(R.id.insulin_correction)
    StickHintInput inputCorrection;
    @BindView(R.id.insulin_basal)
    StickHintInput inputBasal;

    public InsulinView(Context context) {
        super(context, type.INSULIN);
    }

    public InsulinView(Context context, Insulin insulin) {
        super(context, insulin);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_insulin;
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void setValues() {
        inputBolus.setText(measurement.getValuesForUI()[0]);
        inputCorrection.setText(measurement.getValuesForUI()[1]);
        inputBasal.setText(measurement.getValuesForUI()[2]);
    }

    @Override
    protected boolean isValid() {
        boolean isValid = true;

        String bolus = inputBolus.getText().trim();
        String correction = inputCorrection.getText().trim();
        String basal = inputBasal.getText().trim();

        if (StringUs.isBlank(bolus) && StringUs.isBlank(correction) && StringUs.isBlank(basal)) {
            inputBolus.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            if (!StringUs.isBlank(bolus)) {
                isValid = PreferenceHelper.getInstance().isValueValid(inputBolus.getInputView(), type.INSULIN);
            }
            if (!StringUs.isBlank(correction)) {
                isValid = PreferenceHelper.getInstance().isValueValid(inputCorrection.getInputView(), type.INSULIN, true);
            }
            if (!StringUs.isBlank(basal)) {
                isValid = PreferenceHelper.getInstance().isValueValid(inputBasal.getInputView(), type.INSULIN);
            }
        }
        return isValid;
    }

    @Override
    public mesoration getMeasurement() {
        if (isValid()) {
            measurement.setValues(
                    inputBolus.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    FloatUs.parseNumber(inputBolus.getText())) : 0,
                    inputCorrection.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    FloatUs.parseNumber(inputCorrection.getText())) : 0,
                    inputBasal.getText().length() > 0 ?
                            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                                    measurement.getCategory(),
                                    FloatUs.parseNumber(inputBasal.getText())) : 0);
            return measurement;
        } else {
            return null;
        }
    }
}