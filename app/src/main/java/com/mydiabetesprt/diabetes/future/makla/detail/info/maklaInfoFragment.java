package com.mydiabetesprt.diabetes.future.makla.detail.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.InterstitialAd;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.future.makla.BaseFoodFragment;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.event.data.maklaDeletedEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class maklaInfoFragment extends BaseFoodFragment {
    Boolean y=true;
    private InterstitialAd mInterstitialAd;   private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;
    @BindView(R.id.makla_brand) TextView brand;
    @BindView(R.id.makla_ingredients) TextView ingredients;
    @BindView(R.id.makla_value) TextView value;
    @BindView(R.id.makla_labels) ViewGroup labels;

    public maklaInfoFragment() {
        super(R.layout.fragment_food_info, R.string.info, R.drawable.ic_category_meal, -1);

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
    public void onBackPressed() {

    }

    @Override
    public void onBackButtonPressed() {

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        ingredients.setOnClickListener(view -> ingredients.setMaxLines(Integer.MAX_VALUE));

        Food makla = getmakla();
        if (makla != null) {
            String placeholder = getString(R.string.placeholder);
            brand.setText(TextUtils.isEmpty(makla.getBrand()) ? placeholder : makla.getBrand());
            ingredients.setText(TextUtils.isEmpty(makla.getIngredients()) ? placeholder : makla.getIngredients());

            float mealValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                    type.MEAL,
                    makla.getCarbohydrates());
            value.setText(String.format("%s %s", FloatUs.parseFloat(mealValue), PreferenceHelper.getInstance().getLabelForMealPer100g(getContext())));

            labels.removeAllViews();
            if (makla.getLabels() != null && makla.getLabels().length() > 0) {
                labels.setVisibility(View.VISIBLE);
                for (String label : makla.getLabels().split(",")) {
                    labels.addView(new maklaInfoLabelView(getContext(), label));
                }
            } else {
                labels.setVisibility(View.GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(maklaDeletedEvent event) {
        if (getmakla().equals(event.context)) {
            finish();
        }
    }
}
