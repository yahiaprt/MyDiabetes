package com.yahia.healthysiabires.partage.data.validation;

import android.content.Context;
import android.text.Editable;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.partage.view.texteditor.LocalisedNumeroEditText;

/**
 * Created by Filip on 05.11.13.
 */
public class Validateur {

    public static boolean containsNumber(String input) {
        return input.matches(".*\\d.*");
    }

    public static boolean validateEditTextEvent(Context context, LocalisedNumeroEditText editText, type category, boolean checkForHint) {
        String value = editText.getText().toString();

        if (value.length() > 0) {
            return validateEventValue(context, editText, category, value);

        } else if (checkForHint) {
            // Check for Hint value
            CharSequence hint = editText.getHint();
            if (hint != null && hint.toString().length() > 0) {
                return validateEventValue(context, editText, category, hint.toString());

            } else {
                editText.setError(context.getString(R.string.validator_value_empty));
                return false;
            }

        } else {
            editText.setError(context.getString(R.string.validator_value_empty));
            return false;
        }
    }

    private static boolean validateEventValue(Context context, LocalisedNumeroEditText editText, type category, String value) {
        editText.setError(null);

        boolean isValid = true;
        if (containsNumber(value)) {
            float parsedValue = FloatUs.parseNumber(value);
            float defaultValue = PreferenceHelper.getInstance().formatCustomToDefaultUnit(category, parsedValue);
            if (!PreferenceHelper.getInstance().validateEventValue(category, defaultValue)) {
                editText.setError(context.getString(R.string.validator_value_unrealistic));
                isValid = false;
            }
        } else {
            editText.setError(context.getString(R.string.validator_value_number));
            isValid = false;
        }
        return isValid;
    }

    public static boolean validateEditTextFactor(Context context, LocalisedNumeroEditText editText, boolean canBeEmpty) {
        Editable editable = editText.getText();

        if (editable == null) {
            throw new IllegalArgumentException();
        }

        String value = editable.toString();
        if (value.length() > 0) {
            return validateFactor(context, editText, value);
        } else {

            if (canBeEmpty) {
                return true;
            } else {
                // Check for Hint value
                CharSequence charSequence = editText.getHint();

                if (charSequence != null && charSequence.toString().length() > 0) {
                    return validateFactor(context, editText, charSequence.toString());
                } else {
                    editText.setError(context.getString(R.string.validator_value_empty));
                    return false;
                }
            }
        }
    }

    private static boolean validateFactor(Context context, LocalisedNumeroEditText editText, String value) {
        if (!containsNumber(value)) {
            editText.setError(context.getString(R.string.validator_value_number));
            return false;
        }

        float parsedValue = FloatUs.parseNumber(value);
        if (parsedValue < 0.1f || parsedValue > 20) {
            editText.setError(context.getString(R.string.validator_value_unrealistic));
            return false;
        }

        editText.setError(null);
        return true;
    }
}