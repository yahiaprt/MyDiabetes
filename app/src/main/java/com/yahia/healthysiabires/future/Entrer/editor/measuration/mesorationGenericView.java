package com.yahia.healthysiabires.future.Entrer.editor.measuration;

import android.content.Context;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.view.texteditor.StickHintInput;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.partage.data.premier.StringUs;

import butterknife.BindView;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class mesorationGenericView<T extends mesoration> extends mesorationAbstractView<T> {

    @BindView(R.id.value)
    StickHintInput inputView;

    @Deprecated
    public mesorationGenericView(Context context) {
        super(context);
    }

    public mesorationGenericView(Context context, T measurement) {
        super(context, measurement);
    }

    public mesorationGenericView(Context context, type category) {
        super(context, category);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list_item_measurement_generic;
    }

    @Override
    protected void initLayout() {
        inputView.setHint(PreferenceHelper.getInstance().getUnitAcronym(measurement.getCategory()));
    }

    @Override
    protected void setValues() {
        inputView.setText(measurement.getValuesForUI()[0]);
    }

    @Override
    protected boolean isValid() {
        boolean isValid;
        String input = inputView.getText();
        if (StringUs.isBlank(input)) {
            inputView.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else {
            isValid = PreferenceHelper.getInstance().isValueValid(inputView.getInputView(), measurement.getCategory());
        }
        return isValid;
    }

    @Override
    public mesoration getMeasurement() {
        if (isValid()) {
            float value = FloatUs.parseNumber(inputView.getText());
            value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(measurement.getCategory(), value);
            measurement.setValues(value);
            return measurement;
        } else {
            return null;
        }
    }
    
}