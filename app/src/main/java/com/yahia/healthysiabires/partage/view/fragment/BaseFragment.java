package com.yahia.healthysiabires.partage.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yahia.healthysiabires.healthysiabiresApplication;
import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryTagDao;
import com.yahia.healthysiabires.partage.data.database.ydk.mesorationydk;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.data.EntreAddedEt;
import com.yahia.healthysiabires.partage.event.data.EntreDeletedEt;
import com.yahia.healthysiabires.future.navigation.MainActivity;
import com.yahia.healthysiabires.future.navigation.OnFragmentChangeListener;
import com.yahia.healthysiabires.future.navigation.ToolbarBehavior;
import com.yahia.healthysiabires.partage.view.activity.BaseActivity;
import com.yahia.healthysiabires.partage.view.ViewUs;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements ToolbarBehavior {

    @LayoutRes private int layoutResourceId;
    @MenuRes private int menuResId;

    private String title;

    private BaseFragment() {
        // Forbidden
    }

    public BaseFragment(@LayoutRes int layoutResourceId, @StringRes int titleResId, @MenuRes int menuResId) {
        this();
        this.layoutResourceId = layoutResourceId;
        this.title = healthysiabiresApplication.getContext().getString(titleResId);
        this.menuResId = menuResId;
    }

    public BaseFragment(@LayoutRes int layoutResourceId, @StringRes int titleResId) {
        this(layoutResourceId, titleResId, -1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResourceId, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View titleView = getActivity() != null && getActivity() instanceof BaseActivity ? ((BaseActivity) getActivity()).getTitleView() : null;
        if (titleView != null) {
            if (this instanceof ToolbarCallback) {
                titleView.setClickable(true);
                titleView.setOnClickListener(childView -> ((ToolbarCallback) BaseFragment.this).action());
            } else {
                titleView.setClickable(false);
            }
        }
    }

//    public abstract void onBackPressed();
//
//    public abstract void onBackButtonPressed();

    @Override
    public void onResume() {
        super.onResume();
        ets.register(this);

        if (getActivity() instanceof OnFragmentChangeListener) {
            ((OnFragmentChangeListener) getActivity()).onFragmentChanged(this);
        }
    }

    @Override
    public void onDestroy() {
        ets.unregister(this);
        super.onDestroy();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (menuResId >= 0) {
            inflater.inflate(menuResId, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (getActivity() != null) {
            getActivity().setTitle(title);
        }
    }

    public void setTitle(@StringRes int titleResId) {
        setTitle(getString(titleResId));
    }

    protected void showFragment(BaseFragment fragment) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(fragment, null, true);
        }
    }

    protected void finish() {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            } else {
                getActivity().finish();
            }
        }
    }

    interface ToolbarCallback {
        void action();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final EntreDeletedEt event) {
        ViewUs.showSnackbar(getView(), getString(R.string.entry_deleted), v -> {
            Entry entry = event.context;
            EntryDao.getInstance().createOrUpdate(entry);
            for (mesoration measurement : entry.getMeasurementCache()) {
                measurement.setEntry(entry);
                mesorationydk.getInstance(measurement.getClass()).createOrUpdate(measurement);
            }
            for (EntryTag entryTag : event.entryTags) {
                entryTag.setEntry(entry);
                EntryTagDao.getInstance().createOrUpdate(entryTag);
            }
            ets.post(new EntreAddedEt(entry, event.entryTags, event.FoodEatenList));
        });
    }
}
