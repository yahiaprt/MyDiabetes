package com.yahia.healthysiabires.future.Entrer.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.partage.view.recyclerview.adapter.EndAdapter;
import com.yahia.healthysiabires.future.log.entry.LogEntryListItem;

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