package com.mydiabetesprt.diabetes.future.Entrer.editor.measuration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.mesoration;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Insulin;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.database.entity.tension;
import com.mydiabetesprt.diabetes.partage.view.swiper1.Swiper1DismissTouchListener;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 24.09.2015.
 */
public class mesorationView<T extends mesoration> extends CardView implements CompoundButton.OnCheckedChangeListener, Swiper1DismissTouchListener.DismissCallbacks {

    @BindView(R.id.image_showcase) ImageView imageViewShowcase;
    @BindView(R.id.image_category) ImageView imageViewCategory;
    @BindView(R.id.category) TextView textViewCategory;
    @BindView(R.id.layout_content) ViewGroup content;
    @BindView(R.id.checkbox_pin) CheckBox checkBoxPin;
    @BindView(R.id.button_delete) View buttonRemove;

    private type category;
    @Nullable private T measurement;
    @Nullable private Food makla;

    private OnCategoryRemovedListener onCategoryRemovedListener;

    @Deprecated
    public mesorationView(Context context) {
        super(context);
    }

    public mesorationView(Context context, @NonNull T measurement) {
        super(context);
        this.category = measurement.getCategory();
        this.measurement = measurement;
        init();
    }

    public mesorationView(Context context, @NonNull Food makla) {
        super(context);
        this.category = type.MEAL;
        this.makla = makla;
        init();
    }

    public mesorationView(Context context, @NonNull type category) {
        super(context);
        this.category = category;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_item_measurement, this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initLayout();
        initData();
    }

    private void initLayout() {
        setRadius(getContext().getResources().getDimension(R.dimen.card_corner_radius));
        setCardElevation(getContext().getResources().getDimension(R.dimen.card_elevation));
        setUseCompatPadding(true);

        setOnTouchListener(new Swiper1DismissTouchListener(this, null, this));
        checkBoxPin.setChecked(PreferenceHelper.getInstance().isCategoryPinned(category));
        checkBoxPin.setOnCheckedChangeListener(this);
    }

    private void initData() {
        String categoryName = getContext().getString(category.getStringResId());
        imageViewShowcase.setImageResource(category.getShowcaseImageResourceId());
        imageViewCategory.setImageResource(category.getIconImageResourceId());
        textViewCategory.setText(categoryName);
        buttonRemove.setContentDescription(String.format(getContext().getString(R.string.remove_placeholder), categoryName));

        boolean isUpdating = measurement != null;
        switch (category) {
            case INSULIN:
                content.addView(isUpdating ?
                        new InsulinView(getContext(), (Insulin) measurement) :
                        new InsulinView(getContext()));
                break;
            case MEAL:
                content.addView(isUpdating ?
                        new MealView(getContext(), (Meal) measurement) :
                        new MealView(getContext(), makla));
                break;
            case PRESSURE:
                content.addView(isUpdating ?
                        new tensionPressureView(getContext(), (tension) measurement) :
                        new tensionPressureView(getContext()));
                break;
            default:
                content.addView(isUpdating ?
                        new mesorationGenericView<>(getContext(), measurement) :
                        new mesorationGenericView<>(getContext(), category));
        }
    }

    public mesoration getMeasurement() {
        View childView = content.getChildAt(0);
        if (childView instanceof mesorationAbstractView) {
            return ((mesorationAbstractView) childView).getMeasurement();
        } else {
            return null;
        }
    }

    public void setOnCategoryRemovedListener(OnCategoryRemovedListener onCategoryRemovedListener) {
        this.onCategoryRemovedListener = onCategoryRemovedListener;
    }

    private void togglePinnedCategory(final boolean isPinned) {
        int textResId = isPinned ? R.string.category_pin_confirm : R.string.category_unpin_confirm;
        String categoryString = getContext().getString(category.getStringResId());
        String confirmation = String.format(getContext().getString(textResId), categoryString);
        ViewUs.showSnackbar(mesorationView.this, confirmation, view -> {
            checkBoxPin.setOnCheckedChangeListener(null);
            checkBoxPin.setChecked(!isPinned);
            PreferenceHelper.getInstance().setCategoryPinned(category, !isPinned);
            checkBoxPin.setOnCheckedChangeListener(mesorationView.this);
        });
        PreferenceHelper.getInstance().setCategoryPinned(category, isPinned);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_delete)
    protected void remove() {
        if (onCategoryRemovedListener != null) {
            onCategoryRemovedListener.onRemove(category);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        togglePinnedCategory(isChecked);
    }

    @Override
    public boolean canDismiss(Object token) {
        return true;
    }

    @Override
    public void onDismiss(View view, Object token) {
        remove();
    }

    public interface OnCategoryRemovedListener {
        void onRemove(type category);
    }
}
