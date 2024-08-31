package com.yahia.healthysiabires.future.log;


import android.os.AsyncTask;

import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryTagDao;
import com.yahia.healthysiabires.partage.data.database.ydk.FoodEatenydk;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.future.log.empty.LogEmptyListItem;
import com.yahia.healthysiabires.future.log.entry.LogEntryListItem;
import com.yahia.healthysiabires.future.log.mois.LogMonthListItem;
import com.yahia.healthysiabires.partage.view.recyclerview.adapter.EndAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

class LogFetchDataTask extends AsyncTask<Void, Integer, List<LogListItem>> {

    private DateTime startDate;
    private boolean scrollingDown;
    private boolean isInitializing;
    private Listener<List<LogListItem>> listener;

    LogFetchDataTask(DateTime startDate, boolean scrollingDown, boolean isInitializing, Listener<List<LogListItem>> listener) {
        this.startDate = startDate;
        this.scrollingDown = scrollingDown;
        this.isInitializing = isInitializing;
        this.listener = listener;
    }

    @Override
    protected List<LogListItem> doInBackground(Void... params) {
        List<LogListItem> listItems = getListItems(startDate, scrollingDown);
        if (isInitializing) {
            boolean newIsScrollingDown = !scrollingDown;
            int index = newIsScrollingDown ? listItems.size() : 0;
            listItems.addAll(index, getListItems(newIsScrollingDown ? startDate.plusDays(1) : startDate.minusDays(1), newIsScrollingDown));
        }
        return listItems;
    }

    @Override
    protected void onPostExecute(List<LogListItem> result) {
        listener.onFinish(result, scrollingDown);
    }

    private List<LogListItem> getListItems(DateTime startDate, boolean scrollingDown) {
        List<LogListItem> listItems = new ArrayList<>();
        DateTime date = startDate;
        boolean loadMore = true;

        while (loadMore) {
            List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(date);
            for (Entry entry : entries) {
                List<mesoration> measurements = EntryDao.getInstance().getMeasurements(entry);
                entry.setMeasurementCache(measurements);
            }

            if (entries.size() > 0) {
                LogEntryListItem firstListItemEntryOfDay = null;

                for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
                    Entry entry = entries.get(entryIndex);
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    List<FoodEaten> FoodEaten = new ArrayList<>();
                    for (mesoration measurement : entry.getMeasurementCache()) {
                        if (measurement instanceof Meal) {
                            FoodEaten.addAll(FoodEatenydk.getInstance().getAll((Meal) measurement));
                        }
                    }
                    LogEntryListItem listItemEntry = new LogEntryListItem(entry, entryTags, FoodEaten);

                    if (entryIndex == 0) {
                        firstListItemEntryOfDay = listItemEntry;
                    }

                    listItemEntry.setFirstListItemEntryOfDay(firstListItemEntryOfDay);
                    listItems.add(scrollingDown ? listItems.size() : entryIndex, listItemEntry);
                }

            } else {
                listItems.add(scrollingDown ? listItems.size() : 0, new LogEmptyListItem(date));
            }

            boolean isFirstDayOfMonth = date.dayOfMonth().get() == 1;
            if (isFirstDayOfMonth) {
                listItems.add(scrollingDown ? listItems.size() - 1 : 0, new LogMonthListItem(date));
            }

            loadMore = listItems.size() < (EndAdapter.BULK_SIZE);
            date = scrollingDown ? date.plusDays(1) : date.minusDays(1);
        }

        return listItems;
    }

    interface Listener<T> {
        void onFinish(T result, boolean scrollingDown);
    }
}