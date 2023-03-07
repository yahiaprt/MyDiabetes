package com.mydiabetesprt.diabetes.future.makla.input;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.ForeignCollection;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.future.makla.search.maklaSearchActivity;
import com.mydiabetesprt.diabetes.future.makla.search.maklaSearchFragment;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.data.premier.StringUs;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.ui.FoodEatenRemovedEvent;
import com.mydiabetesprt.diabetes.partage.event.ui.FoodEatenUpdatedEvent;
import com.mydiabetesprt.diabetes.partage.event.ui.FoodSelectedEvent;
import com.mydiabetesprt.diabetes.partage.view.texteditor.StickHintInput;
import com.mydiabetesprt.diabetes.partage.view.recyclerview.decorations.LiniarDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 13.10.2016.
 */

public class maklaInputView extends LinearLayout {

    private static final int ANIMATION_DURATION_IN_MILLIS = 750;

    @BindView(R.id.makla_input_icon) ImageView icon;
    @BindView(R.id.makla_input_row) ViewGroup inputRow;
    @BindView(R.id.makla_input_value_input) StickHintInput valueInput;
    @BindView(R.id.makla_input_value_calculated_integral) TickerView valueCalculatedIntegral;
    @BindView(R.id.makla_input_value_calculated_point) TextView valueCalculatedPoint;
    @BindView(R.id.makla_input_value_calculated_fractional) TickerView valueCalculatedFractional;
    @BindView(R.id.makla_input_value_sign) TextView valueSign;
    @BindView(R.id.makla_input_list) RecyclerView maklaList;

    private maklaInputListAdapter adapter;
    private Meal meal;

    private boolean showIcon;

    public maklaInputView(Context context) {
        super(context);
        init();
    }

    public maklaInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public maklaInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.maklaInputView);
        try {
            showIcon = typedArray.getBoolean(R.styleable.maklaInputView_showIcon, false);
        } finally {
            typedArray.recycle();
        }
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ets.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ets.unregister(this);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_food_input, this);

        if (!isInEditMode()) {
            ButterKnife.bind(this);
            meal = new Meal();

            icon.setVisibility(showIcon ? VISIBLE : GONE);
            inputRow.setMinimumHeight(getResources().getDimensionPixelSize(showIcon ? R.dimen.height_element_large : R.dimen.height_element));

            valueInput.setHint(PreferenceHelper.getInstance().getUnitName(type.MEAL));
            String numberList = TickerUtils.provideNumberList();
            valueCalculatedIntegral.setCharacterLists(numberList);
            valueCalculatedFractional.setCharacterLists(numberList);
            valueCalculatedIntegral.setAnimationDuration(ANIMATION_DURATION_IN_MILLIS);
            valueCalculatedFractional.setAnimationDuration(ANIMATION_DURATION_IN_MILLIS);

            adapter = new maklaInputListAdapter(getContext());
            maklaList.setLayoutManager(new LinearLayoutManager(getContext()));
            maklaList.addItemDecoration(new LiniarDividerItemDecoration(getContext()));
            maklaList.setAdapter(adapter);
        }
    }

    public void setupWithMeal(Meal meal) {
        this.meal = meal;
        this.valueInput.setText(meal.getValuesForUI()[0]);
        addItems(meal.getFoodEaten());
    }

    public boolean isValid() {
        boolean isValid = true;

        String input = valueInput.getText().trim();

        if (StringUs.isBlank(input) && !adapter.hasInput()) {
            valueInput.setError(getContext().getString(R.string.validator_value_empty));
            isValid = false;
        } else if (!StringUs.isBlank(input)) {
            isValid = PreferenceHelper.getInstance().isValueValid(valueInput.getInputView(), type.MEAL);
        }
        return isValid;
    }

    public Meal getMeal() {
        if (isValid()) {
            meal.setValues(valueInput.getText().length() > 0 ?
                    PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                            meal.getCategory(),
                            FloatUs.parseNumber(valueInput.getText())) : 0);
            List<FoodEaten> FoodEatenCache = new ArrayList<>();
            for (FoodEaten FoodEaten : adapter.getItems()) {
                if (FoodEaten.getAmountInGrams() > 0) {
                    FoodEatenCache.add(FoodEaten);
                }
            }
            meal.setFoodEatenCache(FoodEatenCache);
            return meal;
        } else {
            return null;
        }
    }

    private void update() {
        updateVisibility();

        float carbohydrates = adapter.getTotalCarbohydrates();
        float meal = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.MEAL, carbohydrates);

        int integral = (int) meal;
        int fractional = Math.round((meal - integral) * 100);

        String integralString = String.valueOf(integral);
        // TODO: Trim ending zeros for fractional part
        String fractionalString = String.valueOf(fractional);

        valueCalculatedIntegral.setText(integralString, true);
        valueCalculatedFractional.setText(fractionalString, true);
    }

    private void updateVisibility() {
        boolean hasFoodEaten = adapter.hasInput();
        valueCalculatedIntegral.setVisibility(hasFoodEaten ? VISIBLE : GONE);
        valueCalculatedPoint.setVisibility(hasFoodEaten ? VISIBLE : GONE);
        valueCalculatedPoint.setText(FloatUs.getDecimalSeparator());
        valueCalculatedFractional.setVisibility(hasFoodEaten ? VISIBLE : GONE);
        valueSign.setVisibility(hasFoodEaten ? VISIBLE : GONE);
        valueCalculatedIntegral.setText(null);
        valueCalculatedFractional.setText(null);
    }

    public void addItem(FoodEaten FoodEaten) {
        if (FoodEaten != null) {
            int position = 0;
            adapter.addItem(position, FoodEaten);
            adapter.notifyItemInserted(position);
            update();
        }
    }

    public void addItem(Food makla) {
        if (makla != null) {
            FoodEaten FoodEaten = new FoodEaten();
            FoodEaten.setmakla(makla);
            FoodEaten.setMeal(meal);
            addItem(FoodEaten);
        }
    }

    public void addItems(ForeignCollection<FoodEaten> FoodEatenList) {
        if (FoodEatenList != null && FoodEatenList.size() > 0) {
            int oldCount = adapter.getItemCount();
            for (FoodEaten FoodEaten : FoodEatenList) {
                adapter.addItem(FoodEaten);
            }
            adapter.notifyItemRangeInserted(oldCount, adapter.getItemCount());
            update();
        }
    }

    public void clear() {
        int itemCount = adapter.getItemCount();
        adapter.clear();
        adapter.notifyItemRangeRemoved(0, itemCount);
        valueInput.setText(null);
        updateVisibility();
    }

    public void removeItem(int position) {
        adapter.removeItem(position);
        adapter.notifyItemRemoved(position);
        update();
    }

    public void updateItem(FoodEaten FoodEaten, int position) {
        adapter.updateItem(position, FoodEaten);
        adapter.notifyItemChanged(position);
        update();
    }

    public float getTotalCarbohydrates() {
        return getInputCarbohydrates() + getCalculatedCarbohydrates();
    }

    public float getInputCarbohydrates() {
        float input = FloatUs.parseNumber(valueInput.getText());
        return PreferenceHelper.getInstance().formatCustomToDefaultUnit(type.MEAL, input);
    }

    public float getCalculatedCarbohydrates() {
        return adapter.getTotalCarbohydrates();
    }

    public List<FoodEaten> getFoodEatenList() {
        return adapter.getItems();
    }

    @OnClick(R.id.makla_input_button)
    public void searchFormakla() {
        Intent intent = new Intent(getContext(), maklaSearchActivity.class);
        intent.putExtra(maklaSearchFragment.FINISH_ON_SELECTION, true);
        getContext().startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSelectedEvent event) {
        addItem(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodEatenUpdatedEvent event) {
        updateItem(event.context, event.position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodEatenRemovedEvent event) {
        removeItem(event.position);
    }
}
