package com.yahia.healthysiabires.future.timeline;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

   
import com.yahia.healthysiabires.R;
import com.yahia.healthysiabires.future.navigation.MainActivity;
import com.yahia.healthysiabires.partage.event.data.EntreAddedEt;
import com.yahia.healthysiabires.partage.event.data.EntreDeletedEt;
import com.yahia.healthysiabires.partage.event.data.EntreUpdatedEt;
import com.yahia.healthysiabires.partage.event.fichier.restorationImportedEt;
import com.yahia.healthysiabires.partage.event.preference.typePreferenceChangedEt;
import com.yahia.healthysiabires.partage.event.preference.UnitChangedEt;
import com.yahia.healthysiabires.partage.view.fragment.DateFragment;
import com.yahia.healthysiabires.partage.view.ViewUs;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;

public class tempslineFragment extends DateFragment implements tempslineViewPager.Listener {
    Boolean y=true;
     
    @BindView(R.id.viewpager)
    tempslineViewPager viewPager;

    public tempslineFragment() {
        super(R.layout.fragment_timeline, R.string.timeline);

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
        viewPager.setup(getChildFragmentManager(), this);
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public String getTitle() {
        boolean showShortText = !ViewUs.isLandscape(getActivity()) && !ViewUs.isLargeScreen(getActivity());
        String weekDay = showShortText ?
                getDay().dayOfWeek().getAsShortText() :
                getDay().dayOfWeek().getAsText();
        String date = DateTimeFormat.mediumDate().print(getDay());
        return String.format("%s, %s", weekDay, date);
    }

    @Override
    protected void goToDay(DateTime day) {
        super.goToDay(day);
        viewPager.setDay(day);
    }

    @Override
    public void onDaySelected(DateTime day) {
        if (day != null) {
            setDay(day);
            updateLabels();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntreAddedEt event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntreDeletedEt event) {
        super.onEvent(event);
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntreUpdatedEt event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEt event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(restorationImportedEt event) {
        if (isAdded()) {
            goToDay(getDay());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(@SuppressWarnings("unused") typePreferenceChangedEt event) {
        if (isAdded()) {
            viewPager.reset();
        }
    }
}
