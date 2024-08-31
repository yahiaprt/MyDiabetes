package com.yahia.healthysiabires.future.types;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

       
   
import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.preference.typePreferenceChangedEt;
import com.yahia.healthysiabires.partage.view.fragment.BaseFragment;
import com.yahia.healthysiabires.partage.view.recyclerview.dragon.DragonDropItemTouchHelperCallback;

import java.util.List;

import butterknife.BindView;

public class typeListFragment extends BaseFragment implements typeListAdapter.Listener {

    Boolean y=true;
     
    @BindView(R.id.listView) RecyclerView list;

    private typeListAdapter listAdapter;
    private ItemTouchHelper itemTouchHelper;
    private boolean hasChanged;
        

    public typeListFragment() {
        super(R.layout.fragment_categories, R.string.categories, R.menu.categories);


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        setCategories();
    }

  


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        if (hasChanged) {
            ets.post(new typePreferenceChangedEt());
        }
        super.onDestroy();
    }

    private void initLayout() {
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new typeListAdapter(getContext(), this);
        list.setAdapter(listAdapter);
        itemTouchHelper = new ItemTouchHelper(new DragonDropItemTouchHelperCallback(listAdapter));
        itemTouchHelper.attachToRecyclerView(list);
    }

    private void setCategories() {
        listAdapter.addItems(PreferenceHelper.getInstance().getSortedCategories());
        listAdapter.notifyDataSetChanged();
    }

    private void showHelp() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.categories)
                .setMessage(R.string.category_preference_desc)
                .setPositiveButton(R.string.ok, (dlg, which) -> { })
                .create()
                .show();
    }

    @Override
    public void onReorderStart(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onReorderEnd() {
        List<type> categories = listAdapter.getItems();
        for (int sortIndex = 0; sortIndex < categories.size(); sortIndex++) {
            PreferenceHelper.getInstance().setCategorySortIndex(categories.get(sortIndex), sortIndex);
        }
        typeComparatorFactory.getInstance().invalidate();
        hasChanged = true;
    }

    @Override
    public void onCheckedChange() {
        hasChanged = true;
    }
}
