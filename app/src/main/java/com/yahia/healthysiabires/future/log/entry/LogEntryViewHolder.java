package com.yahia.healthysiabires.future.log.entry;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.entity.glycemie;
import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.future.Entrer.editor.EntryEditActivity;
import com.yahia.healthysiabires.future.Entrer.search.EntrerSearchListAdapter;
import com.yahia.healthysiabires.partage.view.recyclerview.viewholder.BaseViewHolder;
import com.yahia.healthysiabires.partage.view.chipsat.ChipsatView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LogEntryViewHolder extends BaseViewHolder<LogEntryListItem> {

    @BindView(R.id.root_layout) protected ViewGroup rootLayout;
    @BindView(R.id.cardview) protected CardView cardView;
    @BindView(R.id.date_time_view) protected TextView dateTimeView;
    @BindView(R.id.note_view) protected TextView noteView;
    @BindView(R.id.makla_view) protected TextView maklaView;
    @BindView(R.id.measurements_layout) public ViewGroup measurementsLayout;
    @BindView(R.id.entry_tags) protected ViewGroup tagsView;

    private EntrerSearchListAdapter.OnSearchItemClickListener listener;

    public LogEntryViewHolder(View view, EntrerSearchListAdapter.OnSearchItemClickListener listener) {
        super(view);
        this.listener = listener;
    }

    @Override
    public void bindData() {
        final LogEntryListItem listItem = getListItem();
        final Entry entry = listItem.getEntry();
        final List<EntryTag> entryTags = listItem.getEntryTags();
        final List<FoodEaten> FoodEatenList = listItem.getFoodEatenList();

        cardView.setOnClickListener(view -> EntryEditActivity.show(getContext(), entry));

        dateTimeView.setText(entry.getDate().toString("HH:mm"));

        if (entry.getNote() != null && entry.getNote().length() > 0) {
            noteView.setVisibility(View.VISIBLE);
            noteView.setText(entry.getNote());
        } else {
            noteView.setVisibility(View.GONE);
        }

        if (FoodEatenList != null && FoodEatenList.size() > 0) {
            List<String> maklaNotes = new ArrayList<>();
            for (FoodEaten FoodEaten : FoodEatenList) {
                String FoodEatenAsString = FoodEaten.print();
                if (FoodEatenAsString != null) {
                    maklaNotes.add(FoodEatenAsString);
                }
            }
            if (maklaNotes.size() > 0) {
                maklaView.setVisibility(View.VISIBLE);
                maklaView.setText(TextUtils.join("\n", maklaNotes));
            } else {
                maklaView.setVisibility(View.GONE);
            }
        } else {
            maklaView.setVisibility(View.GONE);
        }

        tagsView.setVisibility(entryTags.size() > 0 ? View.VISIBLE : View.GONE);
        tagsView.removeAllViews();
        for (EntryTag entryTag : entryTags) {
            final Tag tag = entryTag.getTag();
            if (tag != null) {
                ChipsatView chipView = new ChipsatView(getContext());
                chipView.setText(tag.getName());
                chipView.setOnClickListener(view -> listener.onTagClicked(tag, view));
                tagsView.addView(chipView);
            }
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            measurementsLayout.removeAllViews();
            List<mesoration> measurements = entry.getMeasurementCache();
            if (measurements.size() > 0) {
                measurementsLayout.setVisibility(View.VISIBLE);
                for (mesoration measurement : measurements) {
                    type category = measurement.getCategory();
                    View viewMeasurement = inflater.inflate(R.layout.list_item_log_measurement, measurementsLayout, false);
                    ImageView categoryImage = viewMeasurement.findViewById(R.id.image);
                    int imageResourceId = category.getIconImageResourceId();
                    categoryImage.setImageDrawable(ContextCompat.getDrawable(getContext(), imageResourceId));
                    categoryImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray_dark));
                    TextView value = viewMeasurement.findViewById(R.id.value);
                    value.setText(measurement.print(getContext()));

                    if (category == type.BLOODSUGAR) {
                        glycemie bloodSugar = (glycemie) measurement;
                        if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                            int backgroundColor = ContextCompat.getColor(getContext(), R.color.green);
                            if (bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                                backgroundColor = ContextCompat.getColor(getContext(), R.color.red);
                            } else if (bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                                backgroundColor = ContextCompat.getColor(getContext(), R.color.blue);
                            }
                            categoryImage.setColorFilter(backgroundColor);
                        }
                    }
                    measurementsLayout.addView(viewMeasurement);
                }
            } else {
                measurementsLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getSwipeFlags() {
        return ItemTouchHelper.START | ItemTouchHelper.END;
    }
}
