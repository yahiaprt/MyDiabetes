package com.yahia.healthysiabires.future.timeline.jour.tablo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.view.recyclerview.adapter.BaseAdapter;

/**
 * Created by Filip on 04.11.13.
 */
public class CategoryImageListAdapter extends BaseAdapter<CategoryImageListItem, CategoryImageViewHolder> {

    public CategoryImageListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public CategoryImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryImageViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_table_category_image, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryImageViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}