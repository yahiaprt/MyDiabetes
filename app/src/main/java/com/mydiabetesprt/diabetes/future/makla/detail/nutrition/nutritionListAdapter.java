package com.mydiabetesprt.diabetes.future.makla.detail.nutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.BaseAdapter;

class nutritionListAdapter extends BaseAdapter<nutritionListItem, nutritionViewHolder> {

    nutritionListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public nutritionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new nutritionViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_nutrient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull nutritionViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
