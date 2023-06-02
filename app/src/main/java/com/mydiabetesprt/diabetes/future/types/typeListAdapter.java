package com.mydiabetesprt.diabetes.future.types;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.adapter.BaseAdapter;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.dragon.DragonDropItemTouchHelperCallback;

import java.util.Collections;

class typeListAdapter extends BaseAdapter<type, typeViewHolder> implements DragonDropItemTouchHelperCallback.DragDropListener {

    private Listener listener;

    typeListAdapter(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public typeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new typeViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_category, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final typeViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    @Override
    public void onItemDragEnd(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int position = oldPosition; position < newPosition; position++) {
                Collections.swap(getItems(), position, position + 1);
            }
        } else {
            for (int position = oldPosition; position > newPosition; position--) {
                Collections.swap(getItems(), position, position - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
        listener.onReorderEnd();
    }

    interface Listener {
        void onReorderStart(RecyclerView.ViewHolder viewHolder);
        void onReorderEnd();
        void onCheckedChange();
    }
}
