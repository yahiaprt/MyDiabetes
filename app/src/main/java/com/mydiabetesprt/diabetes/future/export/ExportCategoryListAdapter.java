package com.mydiabetesprt.diabetes.future.export;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.BaseAdapter;

import java.util.ArrayList;

class ExportCategoryListAdapter extends BaseAdapter<ExportCategoryListItem, ExportCategoryViewHolder> {

    ExportCategoryListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ExportCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExportCategoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_export_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ExportCategoryViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    type[] getSelectedCategories() {
        ArrayList<type> selectedCategories = new ArrayList<>();
        for (ExportCategoryListItem item : getItems()) {
            if (item.isCategorySelected()) {
                selectedCategories.add(item.getCategory());
            }
        }
        return selectedCategories.toArray(new type[0]);
    }

    boolean exportmakla() {
        for (ExportCategoryListItem item : getItems()) {
            if (item.getCategory() == type.MEAL) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    boolean splitInsulin() {
        for (ExportCategoryListItem item : getItems()) {
            if (item.getCategory() == type.INSULIN) {
                return item.isExtraSelected();
            }
        }
        return false;
    }

    boolean highlightLimits() {
        for (ExportCategoryListItem item : getItems()) {
            if (item.getCategory() == type.BLOODSUGAR) {
                return item.isExtraSelected();
            }
        }
        return false;
    }
}
