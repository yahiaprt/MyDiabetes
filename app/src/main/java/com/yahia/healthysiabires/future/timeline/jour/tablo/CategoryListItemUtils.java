package com.yahia.healthysiabires.future.timeline.jour.tablo;

import com.yahia.healthysiabires.partage.data.database.entity.type;

import java.util.List;

public class CategoryListItemUtils {

    public static CategoryValueListItem sum(type category, List<CategoryValueListItem> items) {
        CategoryValueListItem item = new CategoryValueListItem(category);
        for (CategoryValueListItem value : items) {
            item.setValueOne(item.getValueOne() + value.getValueOne());
            item.setValueTwo(item.getValueTwo() + value.getValueTwo());
            item.setValueThree(item.getValueThree() + value.getValueThree());
        }
        return item;
    }

    public static CategoryValueListItem avg(type category, List<CategoryValueListItem> items) {
        CategoryValueListItem item = sum(category, items);
        return new CategoryValueListItem(category, item.getValueOne() / items.size(), item.getValueTwo() / items.size(), item.getValueThree() / items.size());
    }
}
