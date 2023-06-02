package com.mydiabetesprt.diabetes.future.preference.usin;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.BaseAdapter;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;

/**
 * Created by Faltenreich on 04.09.2016.
 */
public class usinListAdapter extends BaseAdapter<usinListItem, usinViewHolder> {

    public usinListAdapter(Context context) {
        super(context);
    }

    @Override
    public usinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new usinViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_factor, parent, false));
    }

    @Override
    public void onBindViewHolder(final usinViewHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                usinListItem preference = getItem(holder.getAdapterPosition());
                try {
                    preference.setValue(FloatUs.parseNumber(editable.toString()));
                } catch (NumberFormatException exception) {
                    preference.setValue(-1);
                }
            }
        });
    }
}
