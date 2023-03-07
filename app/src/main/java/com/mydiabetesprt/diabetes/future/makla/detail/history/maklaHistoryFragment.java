package com.mydiabetesprt.diabetes.future.makla.detail.history;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.InterstitialAd;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.future.makla.BaseFoodFragment;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.FoodEatenydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.decorations.LiniarDividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class maklaHistoryFragment extends BaseFoodFragment {
    Boolean y=true;
    private InterstitialAd mInterstitialAd;   private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;
    @BindView(R.id.list) RecyclerView historyList;
    @BindView(R.id.list_placeholder) TextView placeholder;

    private maklaHistoryListAdapter historyAdapter;

    public maklaHistoryFragment() {
        super(R.layout.fragment_food_history, R.string.entries, R.drawable.ic_history_old, -1);


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
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onBackButtonPressed() {

    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void init() {
        Food makla = getmakla();
        if (makla != null) {
            historyList.setLayoutManager(new LinearLayoutManager(getContext()));
            historyList.addItemDecoration(new LiniarDividerItemDecoration(getContext()));
            historyAdapter = new maklaHistoryListAdapter(getContext());
            historyList.setAdapter(historyAdapter);
        }
    }

    private void update() {
        historyAdapter.clear();
        List<FoodEaten> FoodEatenList = FoodEatenydk.getInstance().getAll(getmakla());
        historyAdapter.addItems(FoodEatenList);
        historyAdapter.notifyDataSetChanged();
        placeholder.setVisibility(FoodEatenList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
