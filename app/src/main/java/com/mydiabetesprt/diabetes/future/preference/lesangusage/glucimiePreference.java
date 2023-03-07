package com.mydiabetesprt.diabetes.future.preference.lesangusage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.preference.glycemiePreferenceChangedEt;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.data.validation.Validateur;
import com.mydiabetesprt.diabetes.partage.view.texteditor.LocalisedNumeroEditText;

/**
 * Created by Filip on 04.11.13.
 */
public class glucimiePreference extends EditTextPreference {

    private Context context;
    private SharedPreferences sharedPreferences;

    private LocalisedNumeroEditText editTextValue;

    public glucimiePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_bloodsugar);

        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editTextValue = view.findViewById(R.id.value);
        if(editTextValue == null || editTextValue.getText() == null) {
            throw new Resources.NotFoundException();
        }

        float value = FloatUs.parseNumber(sharedPreferences.getString(getKey(), ""));
        value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.BLOODSUGAR, value);
        editTextValue.setText(FloatUs.parseFloat(value));
        editTextValue.setSelection(editTextValue.getText().length());

        TextView textViewUnit = view.findViewById(R.id.unit);
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitName(type.BLOODSUGAR));
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog alertDialog = (AlertDialog)getDialog();
        if(alertDialog == null || alertDialog.getButton(AlertDialog.BUTTON_POSITIVE) == null)
            throw new Resources.NotFoundException();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Validateur.validateEditTextEvent(context, editTextValue, type.BLOODSUGAR, true)) {
                    alertDialog.dismiss();
                    onDialogClosed(true);
                }
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if(editTextValue == null || editTextValue.getText() == null)
                throw new Resources.NotFoundException();

            float value = FloatUs.parseNumber(editTextValue.getText().toString());
            SharedPreferences.Editor editor = getEditor();
            if(editor == null)
                throw new Resources.NotFoundException();

            value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(type.BLOODSUGAR, value);
            editor.putString(getKey(), Float.toString(value));
            editor.commit();

            ets.post(new glycemiePreferenceChangedEt());
        }
    }
}