package com.yahia.healthysiabires.future.makla.search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yahia.healthysiabires.future.makla.networkage.OpenmaklaFactsService;
import com.yahia.healthysiabires.partage.Helper;
import com.yahia.healthysiabires.partage.data.database.ydk.maklaydk;
import com.yahia.healthysiabires.partage.data.database.ydk.FoodEatenydk;
import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.data.maklaDeletedEvent;
import com.yahia.healthysiabires.partage.event.data.FoodQueryEndedEvent;
import com.yahia.healthysiabires.partage.event.data.FoodQueryStartedEvent;
import com.yahia.healthysiabires.partage.event.networkage.maklaSearchFailedEvent;
import com.yahia.healthysiabires.partage.event.networkage.maklaSearchSucceededEvent;
import com.yahia.healthysiabires.partage.view.recyclerview.decorations.LiniarDividerItemDecoration;
import com.yahia.healthysiabires.partage.view.recyclerview.pinguintion.EndRecyclerViewScrollListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 10.11.2016.
 */

public class maklaSearchListView extends RecyclerView {

    private maklaSearchListAdapter adapter;
    private String query;

    private int offlinePage;
    private int onlinePage;

    public maklaSearchListView(Context context) {
        super(context);
        init();
    }

    public maklaSearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ets.register(this);
        newSearch(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ets.unregister(this);
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

        addItemDecoration(new LiniarDividerItemDecoration(getContext()));

        adapter = new maklaSearchListAdapter(getContext());
        setAdapter(adapter);

        EndRecyclerViewScrollListener listener = new EndRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                search();
            }
        };
        addOnScrollListener(listener);
    }

    int getItemCount() {
        return adapter.getItemCount();
    }

    void clear() {
        int oldCount = adapter.getItemCount();
        adapter.clear();
        adapter.notifyItemRangeRemoved(0, oldCount);
    }

    void newSearch(String query) {
        this.query = query;
        this.offlinePage = 0;
        this.onlinePage = 0;

        clear();
        search();
    }

    private void search() {
        // offlinePage gets invalid after online searches
        boolean isSearchOnline = onlinePage > 0;
        if (isSearchOnline) {
            searchOnline();
        } else {
            searchOffline();
        }
    }

    private void searchOffline() {
        ets.post(new FoodQueryStartedEvent());
        new LoadDataTask().execute();
    }

    private void searchOnline() {
        ets.post(new FoodQueryStartedEvent());
        OpenmaklaFactsService.getInstance().search(query, onlinePage);
    }

    private void addItems(List<maklaSearchListItem> maklaList) {
        boolean showBrandedmakla = PreferenceHelper.getInstance().showBrandedmakla();
        List<maklaSearchListItem> filtered = new ArrayList<>();
        if (!showBrandedmakla) {
            for (maklaSearchListItem listItem : maklaList) {
                if (!listItem.getmakla().isBrandedmakla()) {
                    filtered.add(listItem);
                }
            }
        } else {
            filtered = maklaList;
        }

        boolean hasItems = filtered.size() > 0;
        if (hasItems) {
            int oldSize = adapter.getItemCount();
            int newCount = 0;
            for (maklaSearchListItem listItem : filtered) {
                if (!adapter.getItems().contains(listItem)) {
                    adapter.addItem(listItem);
                    newCount++;
                }
            }
            adapter.notifyItemRangeInserted(oldSize, newCount);
        }
        ets.post(new FoodQueryEndedEvent(hasItems));
    }

    // TODO: Filter on database-level
    private void addmakla(List<Food> maklaList) {
        List<maklaSearchListItem> maklaItemList = new ArrayList<>();

        for (Food Food : maklaList) {
            boolean isSameLanguage = Helper.isSystemLocale(Food.getLanguageCode());
            boolean isNotDeleted = !Food.isDeleted();
            if (isSameLanguage && isNotDeleted) {
                maklaItemList.add(new maklaSearchListItem(Food));
            }
        }

        boolean skipResponse = maklaList.size() > 0 && maklaItemList.size() == 0;
        if (skipResponse) {
            searchOnline();
        } else {
            addItems(maklaItemList);
        }
    }

    private void removeItem(Food makla) {
        for (int position = 0; position < getItemCount(); position++) {
            maklaSearchListItem listItem = adapter.getItem(position);
            if (listItem.getmakla().equals(makla)) {
                adapter.removeItem(position);
                adapter.notifyItemRemoved(position);
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(maklaSearchSucceededEvent event) {
        onlinePage++;
        addmakla(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(maklaSearchFailedEvent event) {
        ets.post(new FoodQueryEndedEvent(false));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(maklaDeletedEvent event) {
        removeItem(event.context);
    }

    // TODO: Make static or extract into separate file
    private class LoadDataTask extends AsyncTask<Void, Void, List<maklaSearchListItem>> {

        @Override
        protected List<maklaSearchListItem> doInBackground(Void... voids) {
            List<maklaSearchListItem> maklaList = new ArrayList<>();
            boolean isInitial = adapter.getItemCount() == 0 && !(query != null && query.length() > 0);

            if (isInitial) {
                List<FoodEaten> FoodEatenList = FoodEatenydk.getInstance().getAllOrdered();
                for (FoodEaten FoodEaten : FoodEatenList) {
                    maklaSearchListItem listItem = new maklaSearchListItem(FoodEaten);
                    if (!maklaList.contains(listItem)) {
                        maklaList.add(listItem);
                    }
                }
            }

            List<Food> maklaAllList = maklaydk.getInstance().search(query, offlinePage);
            for (Food makla : maklaAllList) {
                // Skip makla that has been eaten before
                maklaSearchListItem listItem = new maklaSearchListItem(makla);
                if (!maklaList.contains(listItem)) {
                    maklaList.add(listItem);
                }
            }

            return maklaList;
        }

        @Override
        protected void onPostExecute(List<maklaSearchListItem> maklaList) {
            super.onPostExecute(maklaList);

            if (maklaList.size() > 0) {
                offlinePage++;
                addItems(maklaList);

            } else {
                searchOnline();
            }
        }
    }
}
