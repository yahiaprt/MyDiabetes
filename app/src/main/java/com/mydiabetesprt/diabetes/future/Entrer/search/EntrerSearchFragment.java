package com.mydiabetesprt.diabetes.future.Entrer.search;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.InterstitialAd;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.DataLoader;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.DataLoaderListener;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryTagDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.FoodEatenydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.Tagydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.EntryTag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.database.entity.mesoration;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Tag;
import com.mydiabetesprt.diabetes.partage.view.fragment.BaseFragment;
import com.mydiabetesprt.diabetes.future.log.entry.LogEntryListItem;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.layoutmanager.SafeLiniarLayoutManager;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;
import com.lapism.searchview.SearchView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EntrerSearchFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {
    Boolean y=true;
    private InterstitialAd mInterstitialAd;   private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;
    private static final String TAG = EntrerSearchFragment.class.getSimpleName();
    private static final int SEARCH_INPUT_DELAY_IN_MILLIS = 1000;
    private static final int PAGE_SIZE = 25;

    static final String EXTRA_TAG_ID = "tagId";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.searchEditText_input) EditText searchInput;
    @BindView(R.id.search_list) RecyclerView list;
    @BindView(R.id.search_list_empty) TextView listEmptyView;
    @BindView(R.id.search_list_progress) View progressView;

    private EntrerSearchListAdapter listAdapter;
    private long tagId = -1;
    private int currentPage = 0;

    public EntrerSearchFragment() {
        super(R.layout.fragment_entry_search, R.string.search);



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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        preFillQuery();
    }

    @Override
    public void onBackPressed() {


    }

    @Override
    public void onBackButtonPressed() {

    }

    private void init() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle arguments = getActivity().getIntent().getExtras();
            tagId = arguments.getLong(EXTRA_TAG_ID, -1);
        }
    }

    private void initLayout() {
        list.setLayoutManager(new SafeLiniarLayoutManager(getActivity()));
        listAdapter = new EntrerSearchListAdapter(getContext(), (tag, view) -> { if (isAdded()) searchView.setQuery(tag.getName(), true); });
        listAdapter.setOnEndlessListener(scrollingDown -> { if (scrollingDown) continueSearch(); });
        list.setAdapter(listAdapter);

        searchView.setOnQueryTextListener(this);
        searchView.setOnMenuClickListener(this);
        searchView.setArrowOnly(true);

        invalidateEmptyView();
    }

    private void preFillQuery() {
        if (tagId >= 0) {
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<Tag>() {
                @Override
                public Tag onShouldLoad() {
                    return Tagydk.getInstance().getById(tagId);
                }
                @Override
                public void onDidLoad(Tag tag) {
                    if (tag != null) {
                        searchView.setOnQueryTextListener(null);
                        searchView.setQuery(tag.getName(), false);
                        searchView.setOnQueryTextListener(EntrerSearchFragment.this);
                        newSearch();
                    }
                }
            });
        } else {
            // Workaround to focus EditText onViewCreated
            new Handler().postDelayed(() -> ViewUs.showKeyboard(searchInput), 500);
        }
    }

    private void newSearch() {
        int oldCount = listAdapter.getItemCount();
        if (oldCount > 0) {
            listAdapter.clear();
            listAdapter.notifyItemRangeRemoved(0, oldCount);
        }
        currentPage = 0;

        if (StringUtils.isNotBlank(searchView.getQuery().toString())) {
            progressView.setVisibility(View.VISIBLE);
            continueSearch();
        }
        invalidateEmptyView();
    }

    private void continueSearch() {
        final String query = searchView.getQuery().toString();
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<LogEntryListItem>>() {
            @Override
            public List<LogEntryListItem> onShouldLoad() {
                List<LogEntryListItem> listItems = new ArrayList<>();
                List<Entry> entries = EntryDao.getInstance().search(query, currentPage, PAGE_SIZE);
                for (Entry entry : entries) {
                    List<mesoration> measurements = EntryDao.getInstance().getMeasurements(entry);
                    entry.setMeasurementCache(measurements);
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    List<FoodEaten> FoodEatenList = new ArrayList<>();
                    for (mesoration measurement : measurements) {
                        if (measurement instanceof Meal) {
                            FoodEatenList.addAll(FoodEatenydk.getInstance().getAll((Meal) measurement));
                        }
                    }
                    listItems.add(new LogEntryListItem(entry, entryTags, FoodEatenList));
                }
                return listItems;
            }
            @Override
            public void onDidLoad(List<LogEntryListItem> items) {
                String currentQuery = searchView.getQuery().toString();
                if (query.equals(currentQuery)) {
                    currentPage++;
                    int oldCount = listAdapter.getItemCount();
                    listAdapter.addItems(items);
                    listAdapter.notifyItemRangeInserted(oldCount, items.size());
                    progressView.setVisibility(View.GONE);
                    invalidateEmptyView();
                } else {
                    Log.d(TAG, "Dropping obsolete result for \'" + query + "\' (is now: \'" + currentQuery + "\'");
                }
            }
        });
    }

    private void invalidateEmptyView() {
        listEmptyView.setVisibility(progressView.getVisibility() != View.VISIBLE && listAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        listEmptyView.setText(StringUtils.isBlank(searchView.getQuery().toString()) ? R.string.search_prompt : R.string.no_results_found);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Handled in onQueryTextChange()
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        // Delay search in order to reduce obsolete searches
        new Handler().postDelayed(() -> {
            if (newText.equals(searchView.getQuery().toString())) {
                newSearch();
            }
        }, SEARCH_INPUT_DELAY_IN_MILLIS);
        return false;
    }

    @Override
    public void onMenuClick() {
        finish();
    }
}
