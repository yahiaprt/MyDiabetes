package com.mydiabetesprt.diabetes.partage.view.texteditor;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;

import java.text.DecimalFormatSymbols;

/**
 * EditText that supports both the default and localized decimal separator (e.g. '.' and ',' for de-DE).
 * This view is intended for usage with [InputType.TYPE_NUMBER_FLAG_DECIMAL] only,
 * so every default decimal separator is replaced with its localized counterpart (de-DE: "Hello." -> "Hello,")
 */

public class LocalisedNumeroEditText extends AppCompatEditText implements TextWatcher {

    private static final String TAG = LocalisedNumeroEditText.class.getSimpleName();
    private static final Character DEFAULT_SEPARATOR = '.';
    private static final Character LOCALIZED_SEPARATOR = DecimalFormatSymbols.getInstance().getDecimalSeparator();
    private static final String ACCEPTED_CHARACTERS = String.format("-0123456789%s%s", LOCALIZED_SEPARATOR, DEFAULT_SEPARATOR);

    private final KeyListener keyListener = DigitsKeyListener.getInstance(ACCEPTED_CHARACTERS);

    private boolean restrictToLocalizedSeparator = false;

    public LocalisedNumeroEditText(Context context) {
        super(context);
        initLayout();
    }

    public LocalisedNumeroEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
        initLayout();
    }

    public LocalisedNumeroEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
        initLayout();
    }

    /**
     * @return The input with all of its localized separators replaced by the default separator
     */
    public String getNonLocalizedText() {
        return getText().toString().replace(LOCALIZED_SEPARATOR, DEFAULT_SEPARATOR);
    }

    /**
     *
     * @return The input as number if available, otherwise null
     */
    public Float getNumber() {
        return FloatUs.parseNullableNumber(getNonLocalizedText());
    }

    /**
     * @return True if the input can be casted to a Float
     */
    public boolean hasNumber() {
        return getNumber() != null;
    }

    /**
     * @param restrictToLocalizedSeparator Only localized separators are allowed, if set to true, otherwise default separators are allowed as well
     */
    public void restrictToLocalizedSeparator(boolean restrictToLocalizedSeparator) {
        this.restrictToLocalizedSeparator = restrictToLocalizedSeparator;
    }

    /**
     * @return True if only localized separators are allowed
     */
    public boolean isRestricteydkLocalizedSeparator() {
        return restrictToLocalizedSeparator;
    }

    private void initAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.LocalisedNumeroEditText, 0, 0);
            restrictToLocalizedSeparator = typedArray.getBoolean(R.styleable.LocalisedNumeroEditText_restrictToLocalizedSeparator, false);
            typedArray.recycle();
        }
    }

    private void initLayout() {
        removeTextChangedListener(this);
        if (LocalisedNumeroEditTextUtils.isInputTypeNumberDecimal(getInputType())) {
            super.setKeyListener(keyListener);
            addTextChangedListener(this);
        }
    }

    private void invalidateText(String text) {
        if (LocalisedNumeroEditTextUtils.isInputTypeNumberDecimal(getInputType())) {
            String localized = stripUnnecessarySeparators(invalidateSeparators(text));
            if (!localized.equals(text)) {
                setText(localized);
                setSelection(localized.length());
            }
        }
    }

    private String invalidateSeparators(String text) {
        return restrictToLocalizedSeparator ? text.replace(DEFAULT_SEPARATOR, LOCALIZED_SEPARATOR) : text;
    }

    private String stripUnnecessarySeparators(String text) {
        if (LocalisedNumeroEditTextUtils.countOccurrences(text, LOCALIZED_SEPARATOR, DEFAULT_SEPARATOR) > 1) {
            int firstIndexOfDefaultSeparator = LocalisedNumeroEditTextUtils.firstIndexOfOrLastIndex(text, DEFAULT_SEPARATOR);
            int firstIndexOfLocalizedSeparator = LocalisedNumeroEditTextUtils.firstIndexOfOrLastIndex(text, LOCALIZED_SEPARATOR);
            int firstIndexOfSeparator = Math.min(firstIndexOfDefaultSeparator, firstIndexOfLocalizedSeparator);
            String localized = text.substring(0, firstIndexOfSeparator + 1);
            String localizing = text.substring(firstIndexOfSeparator, text.length())
                    .replace(LOCALIZED_SEPARATOR.toString(), "")
                    .replace(DEFAULT_SEPARATOR.toString(), "");
            return localized + localizing;
        } else {
            return text;
        }
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);

        if (LocalisedNumeroEditTextUtils.isInputTypeNumberDecimal(type) && LOCALIZED_SEPARATOR != DEFAULT_SEPARATOR) {
            Log.w(TAG, "Setting raw input type and key listener as well in order to keep functionality");
            super.setKeyListener(keyListener);
            setRawInputType(type);
        }
    }

    @Override
    public void setKeyListener(KeyListener keyListener) {
        throw new UnsupportedOperationException("Calling setKeyListener() is prohibited, since LocalizedEditText relies on its own key listener");
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        invalidateText(text.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}