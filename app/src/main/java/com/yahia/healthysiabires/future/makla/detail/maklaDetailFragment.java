package com.yahia.healthysiabires.future.makla.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

   
import com.google.android.material.tabs.TabLayout;
import com.yahia.healthysiabires.future.Entrer.editor.EntryEditActivity;
import com.yahia.healthysiabires.R;
import com.yahia.healthysiabires.future.makla.BaseFoodFragment;
import com.yahia.healthysiabires.future.makla.editor.maklaEditActivity;
import com.yahia.healthysiabires.partage.data.database.entity.Food;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public class maklaDetailFragment extends BaseFoodFragment {
    Boolean y=true;
     
    @BindView(R.id.makla_viewpager) ViewPager viewPager;
    @BindView(R.id.makla_tablayout) TabLayout tabLayout;

    public maklaDetailFragment() {
        super(R.layout.fragment_food_detail, R.string.makla, -1, R.menu.food);



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
        init();
        update();
    }

  

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deletemaklaIfConfirmed();
                return true;
            case R.id.action_edit:
                editmakla();
                return true;
            case R.id.action_eat:
                eatmakla();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        Food makla = getmakla();
        if (makla != null) {
            maklaDetailViewPagerAdapter adapter = new maklaDetailViewPagerAdapter(getFragmentManager(), makla);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void update() {
        Food makla = getmakla();
        setTitle(makla != null ? makla.getName() : null);
    }

    private void eatmakla() {
        EntryEditActivity.show(getContext(), getmakla());
    }

    private void editmakla() {
        Intent intent = new Intent(getActivity(), maklaEditActivity.class);
        intent.putExtra(BaseFoodFragment.EXTRA_food_ID, getmakla().getId());
        startActivity(intent);
    }
}
