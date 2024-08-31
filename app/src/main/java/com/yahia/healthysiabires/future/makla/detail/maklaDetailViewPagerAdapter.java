package com.yahia.healthysiabires.future.makla.detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.yahia.healthysiabires.future.makla.BaseFoodFragment;
import com.yahia.healthysiabires.future.makla.detail.history.maklaHistoryFragment;
import com.yahia.healthysiabires.future.makla.detail.info.maklaInfoFragment;
import com.yahia.healthysiabires.future.makla.detail.nutrition.nutritionListFragment;
import com.yahia.healthysiabires.future.navigation.ToolbarBehavior;
import com.yahia.healthysiabires.partage.data.database.entity.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 27.10.2016.
 */

class maklaDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFoodFragment> fragments;

    maklaDetailViewPagerAdapter(FragmentManager fragmentManager, Food makla) {
        super(fragmentManager);
        initWithmakla(makla);
    }

    private void initWithmakla(Food makla) {
        Bundle bundle = new Bundle();
        bundle.putLong(BaseFoodFragment.EXTRA_food_ID, makla.getId());

        fragments = new ArrayList<>();

        maklaInfoFragment detailFragment = new maklaInfoFragment();
        detailFragment.setArguments(bundle);
        fragments.add(detailFragment);

        nutritionListFragment nutritionListFragment = new nutritionListFragment();
        nutritionListFragment.setArguments(bundle);
        fragments.add(nutritionListFragment);

        maklaHistoryFragment maklaHistoryFragment = new maklaHistoryFragment();
        maklaHistoryFragment.setArguments(bundle);
        fragments.add(maklaHistoryFragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return position < fragments.size() ? fragments.get(position) : null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = getItem(position);
        return fragment instanceof ToolbarBehavior ? ((ToolbarBehavior) fragment).getTitle() : fragment.toString();
    }
}
