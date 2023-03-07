package com.mydiabetesprt.diabetes.partage.event.data;

import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.EntryTag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
class BaseEntreEt extends BaseDataEt<Entry> {

    public List<EntryTag> entryTags;
    public List<FoodEaten> FoodEatenList;

    BaseEntreEt(Entry entry, List<EntryTag> entryTags, List<FoodEaten> FoodEatenList) {
        super(entry);
        this.entryTags = entryTags;
        this.FoodEatenList = FoodEatenList;
    }
}
