package com.mydiabetesprt.diabetes.future.tag;

import android.view.View;
import android.widget.TextView;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.synchronisation.DataLoader;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.DataLoaderListener;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryTagDao;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Tag;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

class TagViewHolder extends BaseViewHolder<Tag> {

    @BindView(R.id.tag_name) TextView nameView;
    @BindView(R.id.tag_description) TextView descriptionView;
    @BindView(R.id.tag_button_delete) public View deleteButton;

    TagViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        final Tag tag = getListItem();
        nameView.setText(tag.getName());

        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Long>() {
            @Override
            public Long onShouldLoad() {
                return EntryTagDao.getInstance().count(tag);
            }
            @Override
            public void onDidLoad(Long count) {
                descriptionView.setText(String.format("%d %s", count, getContext().getString(R.string.entries)));
            }
        });
    }
}
