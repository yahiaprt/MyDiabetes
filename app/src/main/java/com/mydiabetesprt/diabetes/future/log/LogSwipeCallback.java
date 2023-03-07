package com.mydiabetesprt.diabetes.future.log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.data.EntreDeletedEt;
import com.mydiabetesprt.diabetes.future.log.entry.LogEntryListItem;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.BaseAdapter;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;

/**
 * Created by Faltenreich on 13.05.2017
 */

class LogSwipeCallback extends ItemTouchHelper.Callback {

    private BaseAdapter adapter;

    LogSwipeCallback(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, viewHolder instanceof BaseViewHolder ? ((BaseViewHolder)viewHolder).getSwipeFlags() : 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Object item = adapter.getItem(viewHolder.getAdapterPosition());
        if (item instanceof LogEntryListItem) {
            LogEntryListItem listItem = (LogEntryListItem) item;
            Entry entry = listItem.getEntry();
            EntryDao.getInstance().delete(entry);
            ets.post(new EntreDeletedEt(entry, listItem.getEntryTags(), listItem.getFoodEatenList()));
        }
    }
}
