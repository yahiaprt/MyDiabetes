package com.yahia.healthysiabires.partage.event.data;

import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class EntreUpdatedEt extends BaseEntreEt {

    public DateTime originalDate;

    public EntreUpdatedEt(Entry entry, List<EntryTag> entryTags, DateTime originalDate, List<FoodEaten> FoodEatenList) {
        super(entry, entryTags, FoodEatenList);
        this.originalDate = originalDate;
    }
}
