package com.mydiabetesprt.diabetes.future.makla.detail.nutrition;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.InterstitialAd;
import com.mydiabetesprt.diabetes.partage.Helper;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.future.makla.BaseFoodFragment;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.decorations.LiniarDividerItemDecoration;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class nutritionListFragment extends BaseFoodFragment {
    Boolean y=true;
    private InterstitialAd mInterstitialAd;   private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;
    @BindView(R.id.makla_list_nutritions) RecyclerView nutritionList;

    private nutritionListAdapter listAdapter;

    public nutritionListFragment() {
        super(R.layout.fragment_food_nutrients, R.string.nutriments, R.drawable.ic_note, -1);


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
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
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
        listAdapter = new nutritionListAdapter(getContext());
    }

    private void initLayout() {
        nutritionList.setLayoutManager(new LinearLayoutManager(getContext()));
        nutritionList.addItemDecoration(new LiniarDividerItemDecoration(getContext()));
        nutritionList.setAdapter(listAdapter);
    }

    private void update() {
        if (getContext() != null) {
            Food makla = getmakla();

            listAdapter.clear();

            for (Food.nutrition nutrition : Food.nutrition.values()) {
                String label = nutrition.getLabel(getContext());
                Float number = nutrition.getValue(makla);

                String value = getContext().getString(R.string.placeholder);
                if (number != null && number >= 0) {
                    value = String.format("%s %s",
                        FloatUs.parseFloat(number),
                        getContext().getString(nutrition.getUnitResId()));
                    if (nutrition == Food.nutrition.ENERGY) {
                        value = String.format("%s %s (%s)",
                            FloatUs.parseFloat(Helper.parseKcalToKj(number)),
                            getContext().getString(R.string.energy_acronym_2),
                            value);
                    }
                }
                listAdapter.addItem(new nutritionListItem(label, value));
            }

            listAdapter.notifyDataSetChanged();
        }
    }
}
