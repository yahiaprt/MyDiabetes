package com.yahia.healthysiabires.future.makla.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

   
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.yahia.healthysiabires.R;
import com.yahia.healthysiabires.future.makla.BaseFoodFragment;
import com.yahia.healthysiabires.future.makla.detail.maklaDetailActivity;
import com.yahia.healthysiabires.future.makla.editor.maklaEditActivity;
import com.yahia.healthysiabires.partage.data.synchronisation.DataLoader;
import com.yahia.healthysiabires.partage.data.synchronisation.DataLoaderListener;
import com.yahia.healthysiabires.partage.data.database.ydk.maklaydk;
import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.data.maklaDeletedEvent;
import com.yahia.healthysiabires.partage.event.data.FoodQueryEndedEvent;
import com.yahia.healthysiabires.partage.event.data.FoodQueryStartedEvent;
import com.yahia.healthysiabires.partage.event.data.maklaSavedEt;
import com.yahia.healthysiabires.partage.event.ui.FoodSelectedEvent;
import com.yahia.healthysiabires.partage.networkage.NetworkageUts;
import com.yahia.healthysiabires.partage.view.ViewUs;
import com.yahia.healthysiabires.partage.view.fragment.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public
class maklaSearchFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {
    Boolean y=true;
     
    public static final String FINISH_ON_SELECTION = "finishOnSelection";

    @BindView(R.id.makla_search_unit) TextView unitTextView;
    @BindView(R.id.makla_search_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.makla_search_list)
    maklaSearchListView list;
    @BindView(R.id.search_view) SearchView searchView;

    @BindView(R.id.makla_search_list_empty) ViewGroup emptyList;
    @BindView(R.id.makla_search_empty_icon) ImageView emptyIcon;
    @BindView(R.id.makla_search_empty_text) TextView emptyText;
    @BindView(R.id.makla_search_empty_description) TextView emptyDescription;
    @BindView(R.id.makla_search_empty_button) Button emptyButton;

    private boolean finishOnSelection;
    private SearchAdapter searchAdapter;

    public maklaSearchFragment() {
        super(R.layout.fragment_food_search, R.string.makla);



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
    }

  

    @Override
    public void onResume() {
        super.onResume();
        ets.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ets.unregister(this);
    }

    private void init() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            finishOnSelection = extras.getBoolean(FINISH_ON_SELECTION);
        }
    }

    private void initLayout() {
        unitTextView.setText(PreferenceHelper.getInstance().getLabelForMealPer100g(getContext()));

        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.green_light, R.color.green_lighter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            query(searchView.getQuery().toString());
        });

        searchView.setOnQueryTextListener(this);
        searchView.setOnMenuClickListener(this);
        searchView.setHint(R.string.makla_search);
        searchView.setArrowOnly(false);

        searchAdapter = new SearchAdapter(getContext());
        searchAdapter.addOnItemClickListener((view, position) -> {
            TextView textView = view.findViewById(R.id.textView_item_text);
            String query = textView.getText().toString();
            searchView.setQuery(query, true);
            searchView.close(true);
        });
        searchView.setAdapter(searchAdapter);

        initSuggestions();
    }

    private void initSuggestions() {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<SearchItem>>() {
            @Override
            public List<SearchItem> onShouldLoad() {
                ArrayList<SearchItem> searchItems = new ArrayList<>();
                for (String recentQuery : PreferenceHelper.getInstance().getInputQueries()) {
                    searchItems.add(new SearchItem(R.drawable.ic_history_old, recentQuery));
                }
                return searchItems;
            }
            @Override
            public void onDidLoad(List<SearchItem> searchItems) {
                searchAdapter.setSuggestionsList(searchItems);
            }
        });
    }

    private void showError(@DrawableRes int iconResId, @StringRes int textResId, @StringRes int descResId, @StringRes int buttonTextResId) {
        emptyList.setVisibility(View.VISIBLE);
        emptyIcon.setImageResource(iconResId);
        emptyText.setText(textResId);
        emptyDescription.setText(descResId);
        emptyButton.setText(buttonTextResId);
    }

    private void onmaklaSelected(Food Food) {
        if (finishOnSelection) {
            finish();
        } else {
            openmakla(Food);
        }
    }

    private void openmakla(Food makla) {
        ets.unregister(this);

        Intent intent = new Intent(getContext(), maklaDetailActivity.class);
        intent.putExtra(BaseFoodFragment.EXTRA_food_ID, makla.getId());
        startActivity(intent);
    }

    private void query(String query) {
        emptyList.setVisibility(View.GONE);
        list.newSearch(query);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.close(true);
        PreferenceHelper.getInstance().addInputQuery(query);
        query(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onMenuClick() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);
        } else {
            finish();
        }
    }

    private void createmakla() {
        startActivity(new Intent(getContext(), maklaEditActivity.class));
    }

    private void showEmptyList() {
        if (NetworkageUts.isOnline(getContext())) {
            showError(R.drawable.ic_sad, R.string.error_no_data, R.string.error_no_data_desc, R.string.makla_add_desc);
        } else {
            showError(R.drawable.ic_wifi, R.string.error_no_connection, R.string.error_no_connection_desc, R.string.try_again);
        }
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        createmakla();
    }

    @OnClick(R.id.makla_search_empty_button)
    void onEmptyButtonClick() {
        // Workaround since CONNECTIVITY_ACTION broadcasts cannot be caught since API level 24
        boolean wasNetworkError = emptyText.getText().toString().equals(getString(R.string.error_no_connection));
        if (wasNetworkError) {
            query(searchView.getQuery().toString());
        } else {
            createmakla();
        }
    }

    @OnClick(R.id.imageView_clear)
    void clearQuery() {
        searchView.setTextOnly(null);
        searchView.close(true);
        query(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSelectedEvent event) {
        onmaklaSelected(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodQueryStartedEvent event) {
        if (list.getItemCount() == 0) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodQueryEndedEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        if (list.getItemCount() == 0) {
            showEmptyList();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(maklaSavedEt event) {
        clearQuery();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final maklaDeletedEvent event) {
        ViewUs.showSnackbar(getView(), getString(R.string.makla_deleted), v -> {
            Food Food = event.context;
            Food.setDeletedAt(null);
            maklaydk.getInstance().createOrUpdate(Food);
            ets.post(new maklaSavedEt(Food));
        });
    }
}
