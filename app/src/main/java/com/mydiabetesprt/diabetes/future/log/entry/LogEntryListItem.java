package com.mydiabetesprt.diabetes.future.log.entry;

import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.EntryTag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.future.log.LogListItem;

import java.util.List;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogEntryListItem extends LogListItem {

    private LogEntryListItem firstListItemEntryOfDay;
    private Entry entry;
    private List<EntryTag> entryTags;
    private List<FoodEaten> FoodEatenList;

    public LogEntryListItem(Entry entry, List<EntryTag> entryTags, List<FoodEaten> FoodEatenList) {
        super(entry.getDate());
        this.entry = entry;
        this.entryTags = entryTags;
        this.FoodEatenList = FoodEatenList;
    }

    public LogEntryListItem getFirstListItemEntryOfDay() {
        return firstListItemEntryOfDay;
    }

    public void setFirstListItemEntryOfDay(LogEntryListItem firstListItemEntryOfDay) {
        this.firstListItemEntryOfDay = firstListItemEntryOfDay;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public List<EntryTag> getEntryTags() {
        return entryTags;
    }

    public void setEntryTags(List<EntryTag> entryTags) {
        this.entryTags = entryTags;
    }

    public List<FoodEaten> getFoodEatenList() {
        return FoodEatenList;
    }

    public void setFoodEatenList(List<FoodEaten> FoodEatenList) {
        this.FoodEatenList = FoodEatenList;
    }
}
