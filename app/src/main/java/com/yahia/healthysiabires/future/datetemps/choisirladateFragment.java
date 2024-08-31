package com.yahia.healthysiabires.future.datetemps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.yahia.healthysiabires.R;import org.joda.time.DateTime;

public class choisirladateFragment extends DialogFragment {

    private static final String DATE_PICKER_FRAGMENT_DATE_SELECTED = "DATE_PICKER_FRAGMENT_DATE_SELECTED";
    private static final String DATE_PICKER_FRAGMENT_DATE_MIN = "DATE_PICKER_FRAGMENT_DATE_MIN";
    private static final String DATE_PICKER_FRAGMENT_DATE_MAX = "DATE_PICKER_FRAGMENT_DATE_MAX";

    public static choisirladateFragment newInstance(@Nullable DateTime selectedDateTime, @Nullable DateTime minDateTime, @Nullable DateTime maxDateTime, DatePickerListener listener) {
        choisirladateFragment fragment = new choisirladateFragment();
        fragment.listener = listener;
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATE_PICKER_FRAGMENT_DATE_SELECTED, selectedDateTime);
        bundle.putSerializable(DATE_PICKER_FRAGMENT_DATE_MIN, minDateTime);
        bundle.putSerializable(DATE_PICKER_FRAGMENT_DATE_MAX, maxDateTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static choisirladateFragment newInstance(DateTime selectedDateTime, DatePickerListener listener) {
        return newInstance(selectedDateTime, null, null, listener);
    }

    private DatePickerListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime selectedDateTime = getArguments() != null ? (DateTime) getArguments().getSerializable(DATE_PICKER_FRAGMENT_DATE_SELECTED) : null;
        selectedDateTime = selectedDateTime != null ? selectedDateTime : DateTime.now();
        DateTime minDateTime = getArguments() != null ? (DateTime) getArguments().getSerializable(DATE_PICKER_FRAGMENT_DATE_MIN) : null;
        DateTime maxDateTime = getArguments() != null ? (DateTime) getArguments().getSerializable(DATE_PICKER_FRAGMENT_DATE_MAX) : null;

        int year = selectedDateTime.getYear();
        // Months are zero-based due to the Calendar.class
        int month = selectedDateTime.getMonthOfYear() - 1;
        final int day = selectedDateTime.getDayOfMonth();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.DateTimePicker, null, year, month, day);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog1, which) -> listener.onDatePicked(null));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog12, which) -> listener.onDatePicked(getDate()));
        if (minDateTime != null) {
            dialog.getDatePicker().setMinDate(minDateTime.getMillis());
        }
        if (maxDateTime != null) {
            dialog.getDatePicker().setMaxDate(maxDateTime.getMillis());
        }
        return dialog;
    }

    public DateTime getDate() {
        DatePicker datePicker = ((DatePickerDialog) getDialog()).getDatePicker();
        return DateTime.now().withYear(datePicker.getYear()).withMonthOfYear(datePicker.getMonth() + 1).withDayOfMonth(datePicker.getDayOfMonth());
    }

    public void show(FragmentManager manager) {
        super.show(manager, choisirladateFragment.class.getSimpleName());
    }

    public interface DatePickerListener {
        void onDatePicked(@Nullable DateTime dateTime);
    }

}