package com.mydiabetesprt.diabetes.partage.event.data;

import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.EntryTag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntreDeletedEt extends BaseEntreEt {

    public EntreDeletedEt(Entry entry, List<EntryTag> entryTags, List<FoodEaten> FoodEatenList) {
        super(entry, entryTags, FoodEatenList);
    }
}
