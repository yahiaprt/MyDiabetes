package com.mydiabetesprt.diabetes.future.types;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.dragon.Dragongable;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.viewholder.BaseViewHolder;
import com.mydiabetesprt.diabetes.partage.view.resource.ColorUs;

import butterknife.BindView;

class typeViewHolder extends BaseViewHolder<type> implements Dragongable {

    @BindView(R.id.background) ViewGroup background;
    @BindView(R.id.titleLabel) TextView titleLabel;
    @BindView(R.id.checkBoxActive) CheckBox activeCheckBox;
    @BindView(R.id.checkBoxPinned) CheckBox pinnedCheckBox;
    @BindView(R.id.dragView) View dragView;

    typeViewHolder(View view, typeListAdapter.Listener listener) {
        super(view);
        activeCheckBox.setOnCheckedChangeListener((v, isChecked) -> {
            PreferenceHelper.getInstance().setCategoryActive(getListItem(), isChecked);
            listener.onCheckedChange();
        });
        pinnedCheckBox.setOnCheckedChangeListener((v, isChecked) -> PreferenceHelper.getInstance().setCategoryPinned(getListItem(), isChecked));
        dragView.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                listener.onReorderStart(typeViewHolder.this);
                v.performClick();
            }
            return false;
        });
    }

    @Override
    protected void bindData() {
        type category = getListItem();
        titleLabel.setText(getContext().getString(category.getStringResId()));
        activeCheckBox.setEnabled(category.isOptional());
        activeCheckBox.setChecked(PreferenceHelper.getInstance().isCategoryActive(category));
        pinnedCheckBox.setChecked(PreferenceHelper.getInstance().isCategoryPinned(category));
        dragView.setVisibility(category.isOptional() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean isDraggable() {
        return getListItem() != type.BLOODSUGAR;
    }

    @Override
    public void onDrag(boolean isDragged) {
        background.setBackgroundColor(isDragged ? ColorUs.getBackgroundSecondary(getContext()) : ColorUs.getBackgroundPrimary(getContext()));
    }
}
