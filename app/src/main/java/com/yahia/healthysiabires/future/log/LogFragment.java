package com.yahia.healthysiabires.future.log;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

   
import com.yahia.healthysiabires.R;
import com.yahia.healthysiabires.future.navigation.MainActivity;
import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.partage.event.data.EntreAddedEt;
import com.yahia.healthysiabires.partage.event.data.EntreDeletedEt;
import com.yahia.healthysiabires.partage.event.data.EntreUpdatedEt;
import com.yahia.healthysiabires.partage.event.fichier.restorationImportedEt;
import com.yahia.healthysiabires.partage.event.preference.typePreferenceChangedEt;
import com.yahia.healthysiabires.partage.event.preference.UnitChangedEt;
import com.yahia.healthysiabires.partage.view.fragment.DateFragment;
import com.yahia.healthysiabires.partage.view.ViewUs;
import com.yahia.healthysiabires.future.Entrer.search.EntrerSearchActivity;
import com.yahia.healthysiabires.future.log.empty.LogEmptyListItem;
import com.yahia.healthysiabires.future.log.entry.LogEntryListItem;
import com.yahia.healthysiabires.partage.view.recyclerview.decorations.StickHeaderDecoration;
import com.yahia.healthysiabires.partage.view.recyclerview.layoutmanager.SafeLiniarLayoutManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;


public class LogFragment extends DateFragment implements LogListAdapter.Listener {
    Boolean y=true;
     
    @BindView(R.id.log_list) RecyclerView recyclerView;
    @BindView(R.id.log_progressbar) ProgressBar progressBar;
   Context context = this.getContext();
    private LogListAdapter listAdapter;
    private StickHeaderDecoration listDecoration;
    private LinearLayoutManager listLayoutManager;

    public LogFragment() {
        super(R.layout.fragment_log, R.string.log);

       //        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//         mInterstitialAd = new InterstitialAd(getContext());
//        mInterstitialAd.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                finish();
//            }
//
//        });
//
//        }
//
//    @Override
//    public void onBackPressed() {
//        showIntewrestial();
//
//    }
//    @Override
//    public void onBackButtonPressed() {
//        if (y=true) {
//            showIntewrestial();
//        } else {
//            getActivity().onBackPressed();
//        }
//    }
//
//    public  void showIntewrestial(){
//        if(mInterstitialAd.isLoaded()){
//            mInterstitialAd.show();
//        }else {finish();
//        }

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        // Fake delay for smoother fragment transitions
        new Handler().postDelayed(() -> goToDay(getDay()), 350);
    }




    @Override
    public String getTitle() {
        String format = ViewUs.isLandscape(getActivity()) || ViewUs.isLargeScreen(getActivity()) ? "MMMM YYYY" : "MMM YYYY";
        return getDay().toString(format);
    }

    private void initLayout() {
        listLayoutManager = new SafeLiniarLayoutManager(getActivity());
        recyclerView.setLayoutManager(listLayoutManager);
        listAdapter = new LogListAdapter(getActivity(), this);
        recyclerView.setAdapter(listAdapter);
        listDecoration = new StickHeaderDecoration(listAdapter, true);
        recyclerView.addItemDecoration(listDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new LogSwipeCallback(listAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Fragment updates
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                DateTime firstVisibleDay = getFirstVisibleDay();
                if (firstVisibleDay != null) {
                    setDay(firstVisibleDay);
                    // Update mois in Toolbar when section is being crossed
                    boolean isScrollingUp = dy < 0;
                    boolean isCrossingMonth = isScrollingUp ?
                            getDay().dayOfMonth().get() == getDay().dayOfMonth().getMaximumValue() :
                            getDay().dayOfMonth().get() == getDay().dayOfMonth().getMinimumValue();
                    if (isCrossingMonth) {
                        updateLabels();
                    }
                }
            }
        });
    }

    private DateTime getFirstVisibleDay() {
        int firstVisibleItemPosition = listLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < listAdapter.getItemCount()) {
            LogListItem item = listAdapter.getItem(listLayoutManager.findFirstVisibleItemPosition());
            return item.getDateTime();
        } else {
            return null;
        }
    }

    @Override
    protected void goToDay(DateTime dateTime) {
        super.goToDay(dateTime);

        int position = listAdapter.getDayPosition(dateTime);
        if (position >= 0) {
            recyclerView.scrollToPosition(position);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(dateTime);
        }
    }

    @Override
    public void onOrderChanges() {
        invalidateSections();
    }

    @Override
    public void onSetupEnd() {
        progressBar.setVisibility(View.GONE);
        goToDay(getDay());
    }

    @Override
    public void onTagClicked(Tag tag, View view) {
        if (isAdded()) {
            EntrerSearchActivity.show(getContext(), tag);
        }
    }

    private void invalidateSections() {
        if (isAdded() && listDecoration != null) {
            listDecoration.clearHeaderCache();
        }
    }

    private void updateHeaderSection(DateTime dateTime) {
        int position = listAdapter.getFirstListItemEntryOfDayPosition(dateTime);
        if (position >= 0) {
            LogEntryListItem firstListItemEntry = (LogEntryListItem) listAdapter.getItem(position);
            while (listAdapter.getItem(position).getDateTime().withTimeAtStartOfDay().isEqual(dateTime.withTimeAtStartOfDay()) &&
                    listAdapter.getItem(position) instanceof LogEntryListItem) {
                LogEntryListItem listItem = (LogEntryListItem) listAdapter.getItem(position);
                listItem.setFirstListItemEntryOfDay(firstListItemEntry);
                position++;
            }
        }
        invalidateSections();
    }

    private void addEntry(Entry entry, List<EntryTag> entryTags, List<FoodEaten> FoodEatenList) {
        if (entry != null) {
            DateTime date = entry.getDate();
            int position = listAdapter.getNextDateTimePosition(date);
            if (position >= 0) {

                // Remove any existing empty view
                int previousPosition = position - 1;
                LogListItem previousListItem = listAdapter.getItem(previousPosition);
                if (previousListItem instanceof LogEmptyListItem && previousListItem.getDateTime().getDayOfYear() == date.getDayOfYear()) {
                    listAdapter.removeItem(previousPosition);
                    listAdapter.notifyItemRemoved(previousPosition);
                    position = previousPosition;
                }

                entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));

                LogEntryListItem listItemEntry = new LogEntryListItem(entry, entryTags, FoodEatenList);
                listAdapter.addItem(position, listItemEntry);
                listAdapter.notifyItemInserted(position);

                updateHeaderSection(date);
            }
        }
    }

    private void removeEntry(Entry entry) {
        if (entry != null) {
            int position = listAdapter.getEntryPosition(entry);
            if (position >= 0) {
                removeEntry(position, entry.getDate());
            }
        }
    }

    private void removeEntry(int position, DateTime date) {
        listAdapter.removeItem(position);

        // Add empty view if there is no entry available anymore for this jour
        boolean hasNoMoreEntries = listAdapter.getFirstListItemEntryOfDayPosition(date) == -1;
        if (hasNoMoreEntries) {
            listAdapter.addItem(position, new LogEmptyListItem(date));
            listAdapter.notifyItemChanged(position);
        } else {
            listAdapter.notifyItemRemoved(position);
        }

        updateHeaderSection(date);
    }

    private void updateEntry(Entry entry, List<EntryTag> entryTags, List<FoodEaten> FoodEatenList, DateTime originalDate) {
        if (entry != null) {
            int originalPosition = listAdapter.getEntryPosition(entry);
            if (originalPosition >= 0) {
                int updatedPosition = listAdapter.getNextDateTimePosition(entry.getDate()) - 1;
                if (originalPosition == updatedPosition) {
                    Object listItem = listAdapter.getItem(originalPosition);
                    if (listItem instanceof LogEntryListItem) {
                        LogEntryListItem listItemEntry = (LogEntryListItem) listItem;
                        listItemEntry.setEntry(entry);
                        listItemEntry.setEntryTags(entryTags);
                        listItemEntry.setFoodEatenList(FoodEatenList);
                        listAdapter.notifyItemChanged(originalPosition);
                    }
                } else {
                    removeEntry(originalPosition, originalDate);
                    addEntry(entry, entryTags, FoodEatenList);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntreAddedEt event) {
        if (isAdded()) {
            addEntry(event.context, event.entryTags, event.FoodEatenList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntreDeletedEt event) {
        super.onEvent(event);
        if (isAdded()) {
            removeEntry(event.context);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntreUpdatedEt event) {
        if (isAdded()) {
            updateEntry(event.context, event.entryTags, event.FoodEatenList, event.originalDate);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") UnitChangedEt event) {
        if (isAdded()) {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") restorationImportedEt event) {
        if (isAdded()) {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") typePreferenceChangedEt event) {
        if (isAdded()) {
            progressBar.setVisibility(View.VISIBLE);
            listAdapter.setup(getDay());
        }
    }
}
