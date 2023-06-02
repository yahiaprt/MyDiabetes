package com.mydiabetesprt.diabetes.future.makla.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class maklaSearchListAdapter extends BaseAdapter<maklaSearchListItem, maklaSearchViewHolder> {

    maklaSearchListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public maklaSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new maklaSearchViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_food_search, parent, false));
    }

    @Override
    public void onBindViewHolder(maklaSearchViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
