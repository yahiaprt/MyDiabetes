package com.yahia.healthysiabires.future.export;

import com.yahia.healthysiabires.partage.data.database.entity.type;

public class ExportCategoryListItem {

    private type category;
    private boolean isCategorySelected;
    private boolean isExtraSelected;

    public ExportCategoryListItem(type category, boolean isCategorySelected, boolean isExtraSelected) {
        this.category = category;
        this.isCategorySelected = isCategorySelected;
        this.isExtraSelected = isExtraSelected;
    }

    public type getCategory() {
        return category;
    }

    public boolean isCategorySelected() {
        return isCategorySelected;
    }

    public void setCategorySelected(boolean categorySelected) {
        isCategorySelected = categorySelected;
    }

    public boolean isExtraSelected() {
        return isExtraSelected;
    }

    public void setExtraSelected(boolean extraSelected) {
        isExtraSelected = extraSelected;
    }
}
