package com.mydiabetesprt.diabetes.future.Entrer.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.Tag;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.EndAdapter;
import com.mydiabetesprt.diabetes.future.log.entry.LogEntryListItem;

public class EntrerSearchListAdapter extends EndAdapter<LogEntryListItem, EntrerSearchViewHolder> {

    private OnSearchItemClickListener listener;

    EntrerSearchListAdapter(Context context, OnSearchItemClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    @NonNull
    public EntrerSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EntrerSearchViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_log_entry, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrerSearchViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(getItem(position));
    }

    public interface OnSearchItemClickListener {
        void onTagClicked(Tag tag, View view);
    }
}