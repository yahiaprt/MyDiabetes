package com.yahia.healthysiabires.future.timeline.jour.tablo;

import com.yahia.healthysiabires.partage.data.database.entity.type;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class CategoryListItem {

    private type category;

    CategoryListItem(type category) {
        this.category = category;
    }

    public type getCategory() {
        return category;
    }
}
