package com.mydiabetesprt.diabetes.future.makla;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.view.fragment.BaseFragment;


import com.mydiabetesprt.diabetes.partage.data.database.ydk.maklaydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.FoodEatenydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.data.maklaDeletedEvent;

/**
 * Created by Faltenreich on 27.09.2016.
 */

public abstract class BaseFoodFragment extends BaseFragment {

    public static final String EXTRA_food_ID = "EXTRA_food_ID";

    private @DrawableRes int icon;
    private Food makla;

    protected BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId, @DrawableRes int icon, @MenuRes int menuResId) {
        super(layoutResId, titleResId, menuResId);
        this.icon = icon;
    }

    protected BaseFoodFragment(@LayoutRes int layoutResId, @StringRes int titleResId, @MenuRes int menuResId) {
        super(layoutResId, titleResId, menuResId);
        this.icon = -1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIntents();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (makla != null) {
            makla = maklaydk.getInstance().getById(makla.getId());
        }
    }

    private void checkIntents() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.getLong(EXTRA_food_ID) >= 0) {
                long maklaId = extras.getLong(EXTRA_food_ID);
                this.makla = maklaydk.getInstance().getById(maklaId);
            }
        }
    }

    protected void deletemaklaIfConfirmed() {
        if (getContext() != null) {
            Food makla = getmakla();
            if (makla != null) {
                long FoodEaten = FoodEatenydk.getInstance().count(makla);
                String message = String.format(getString(R.string.makla_eaten_placeholder), FoodEaten);
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.makla_delete)
                        .setMessage(message)
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {})
                        .setPositiveButton(R.string.delete, (dialog, id) -> deletemakla(makla))
                        .create()
                        .show();
            }
        }
    }

    private void deletemakla(Food makla) {
        maklaydk.getInstance().softDelete(makla);
        ets.post(new maklaDeletedEvent(makla));
        finish();
    }

    protected Food getmakla() {
        return makla;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }
}
