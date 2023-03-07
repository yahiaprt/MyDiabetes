package com.mydiabetesprt.diabetes.future.makla.detail.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class maklaHistoryListAdapter extends BaseAdapter<FoodEaten, maklaHistoryViewHolder> {

    maklaHistoryListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public maklaHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new maklaHistoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_food_eaten, parent, false));
    }

    @Override
    public void onBindViewHolder(maklaHistoryViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
