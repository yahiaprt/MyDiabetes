package com.mydiabetesprt.diabetes.future.Entrer.editor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryTagDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.maklaydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.mesorationydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.Tagydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.EntryTag;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.database.entity.mesoration;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Tag;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.data.EntreAddedEt;
import com.mydiabetesprt.diabetes.partage.event.data.EntreDeletedEt;
import com.mydiabetesprt.diabetes.partage.event.data.EntreUpdatedEt;
import com.mydiabetesprt.diabetes.partage.data.premier.StringUs;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.DataLoader;
import com.mydiabetesprt.diabetes.partage.data.synchronisation.DataLoaderListener;
import com.mydiabetesprt.diabetes.partage.view.activity.BaseActivity;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;
import com.mydiabetesprt.diabetes.future.alarm.AlarmUS;
import com.mydiabetesprt.diabetes.future.datetemps.choisirladateFragment;
import com.mydiabetesprt.diabetes.future.datetemps.choisirletimeUS;
import com.mydiabetesprt.diabetes.future.datetemps.letempsPickerFragment;
import com.mydiabetesprt.diabetes.future.Entrer.editor.measuration.mesorationFloatingActionMenu;
import com.mydiabetesprt.diabetes.future.Entrer.editor.measuration.mesorationListView;
import com.mydiabetesprt.diabetes.future.Entrer.editor.measuration.mesorationView;
import com.mydiabetesprt.diabetes.future.tag.TagAutoCompleteAdapter;
import com.mydiabetesprt.diabetes.partage.view.chipsat.ChipsatView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.chip.ChipGroup;
import com.mydiabetesprt.diabetes.future.makla.BaseFoodFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EntryEditActivity extends BaseActivity implements mesorationFloatingActionMenu.OnFabSelectedListener, mesorationListView.OnCategoryEventListener {

    private static final String TAG = EntryEditActivity.class.getSimpleName();

    public static final String EXTRA_ENTRY_ID = "entryId";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_CATEGORY = "types";

    private static Intent getIntent(Context context) {
        return new Intent(context, EntryEditActivity.class);
    }

    public static void show(Context context) {
        context.startActivity(getIntent(context));
    }

    public static void show(Context context, @Nullable Entry entry) {
        Intent intent = getIntent(context);
        if (entry != null) {
            intent.putExtra(EXTRA_ENTRY_ID, entry.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @Nullable Food makla) {
        Intent intent = getIntent(context);
        if (makla != null) {
            intent.putExtra(BaseFoodFragment.EXTRA_food_ID, makla.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @NonNull DateTime dateTime) {
        Intent intent = getIntent(context);
        intent.putExtra(EXTRA_DATE, dateTime);
        context.startActivity(intent);
    }

    public static void show(Context context, @NonNull type category) {
        Intent intent = getIntent(context);
        intent.putExtra(EXTRA_CATEGORY, category);
        context.startActivity(intent);
    }

    @BindView(R.id.root) View rootView;
    @BindView(R.id.activity_newevent_scrollview) NestedScrollView scrollView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab_menu)
    mesorationFloatingActionMenu fabMenu;
    @BindView(R.id.layout_measurements)
    mesorationListView layoutMeasurements;
    @BindView(R.id.edittext_notes) EditText editTextNotes;
    @BindView(R.id.button_date) Button buttonDate;
    @BindView(R.id.button_time) Button buttonTime;
    @BindView(R.id.entry_button_alarm) Button buttonAlarm;
    @BindView(R.id.entry_alarm_container) ViewGroup containerAlarm;
    @BindView(R.id.entry_tags_input) AutoCompleteTextView tagsInput;
    @BindView(R.id.entry_tags) ChipGroup tagsView;

    private long entryId;
    private long maklaId;
    private DateTime time = DateTime.now();

    private Entry entry;
    private List<EntryTag> entryTags;
    private type[] activeCategories;
    private int alarmInMinutes;

    private TagAutoCompleteAdapter tagAdapter;

    public EntryEditActivity() {
        super(R.layout.activity_entry_edit);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initLayout();
        fetchData();
        addMeasurementForGivenCategory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_edit, menu);
        menu.findItem(R.id.action_delete).setVisible(entryId > 0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            entryId = arguments.getLong(EXTRA_ENTRY_ID);
            maklaId = arguments.getLong(BaseFoodFragment.EXTRA_food_ID);
            if (arguments.get(EXTRA_DATE) != null) {
                time = (DateTime) arguments.getSerializable(EXTRA_DATE);
            }
        }
        activeCategories = PreferenceHelper.getInstance().getActiveCategories();
    }

    private void initLayout() {
        updateDateTime();
        updateAlarm();

        if (entryId > 0) {
            setTitle(getString(R.string.entry_edit));
            containerAlarm.setVisibility(View.GONE);
        } else {
            containerAlarm.setVisibility(View.VISIBLE);
        }

        layoutMeasurements.setOnCategoryEventListener(this);

        fabMenu.setOnFabSelectedListener(this);

        if (entryId <= 0 && maklaId <= 0) {
            addPinnedCategories();
        }
        fabMenu.restock();

        tagsView.setVisibility(View.GONE);
        tagAdapter = new TagAutoCompleteAdapter(this);
        tagsInput.setAdapter(tagAdapter);
        tagsInput.setOnEditorActionListener((textView, action, keyEvent) -> {
            if (action == EditorInfo.IME_ACTION_DONE) {
                String name = textView.getText().toString().trim();
                if (!StringUs.isBlank(name)) {
                    addTag(textView.getText().toString());
                    textView.setText(null);
                }
                return true;
            }
            return false;
        });
        tagsInput.setOnFocusChangeListener((view, hasFocus) -> {
            // Attempt to fix android.view.WindowManager$BaydkkenException
            new Handler().post(() -> {
                try {
                    if (hasFocus) tagsInput.showDropDown();
                } catch (Exception exception) {
                    Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Failed to show dropdown");
                }
            });
        });
        tagsInput.setOnClickListener(view -> tagsInput.showDropDown());
        tagsInput.setOnItemClickListener((adapterView, view, position, l) -> {
            tagsInput.setText(null);
            Tag tag = tagAdapter.getItem(position);
            addTag(tag);
        });
    }

    private void fetchData() {
        if (entryId > 0) {
            fetchEntry(entryId);
        } else if (maklaId > 0) {
            fetchmakla(maklaId);
        }
        fetchTags();
    }

    private void fetchEntry(final long id) {
        DataLoader.getInstance().load(this, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                EntryDao dao = EntryDao.getInstance();
                entry = dao.getById(id);
                if (entry != null) {
                    entry.setMeasurementCache(dao.getMeasurements(entry));
                    entryTags = EntryTagDao.getInstance().getAll(entry);
                }
                return null;
            }

            @Override
            public void onDidLoad(List<Tag> data) {
                initEntry(entry, entryTags);
            }
        });
    }

    private void fetchmakla(final long id) {
        DataLoader.getInstance().load(this, new DataLoaderListener<Food>() {
            @Override
            public Food onShouldLoad() {
                return maklaydk.getInstance().getById(id);
            }

            @Override
            public void onDidLoad(Food makla) {
                if (makla != null) {
                    layoutMeasurements.addMeasurement(makla);
                    fabMenu.ignore(type.MEAL);
                    fabMenu.restock();
                }
            }
        });
    }

    private void fetchTags() {
        DataLoader.getInstance().load(this, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                return Tagydk.getInstance().getRecent();
            }

            @Override
            public void onDidLoad(List<Tag> tags) {
                tagAdapter.addAll(tags);
                tagAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addMeasurementForGivenCategory() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            type category = (type) arguments.getSerializable(EXTRA_CATEGORY);
            if (category != null) {
                addMeasurementView(category);
            }
        }
    }

    private void addPinnedCategories() {
        for (type category : activeCategories) {
            if (PreferenceHelper.getInstance().isCategoryPinned(category) && !layoutMeasurements.hasCategory(category)) {
                layoutMeasurements.addMeasurementAtEnd(category);
            }
        }
    }

    private void initEntry(Entry entry, List<EntryTag> entryTags) {
        if (entry != null) {
            this.entry = entry;
            this.entryTags = entryTags;
            this.time = entry.getDate();

            editTextNotes.setText(entry.getNote());
            layoutMeasurements.addMeasurements(entry.getMeasurementCache());

            if (entryTags != null) {
                for (EntryTag entryTag : entryTags) {
                    Tag tag = entryTag.getTag();
                    if (tag != null) {
                        addTag(entryTag.getTag());
                    }
                }
            }
        } else {
            addPinnedCategories();
        }
        updateDateTime();
    }

    private void toggleSubmitButton(boolean isEnabled) {
        fab.setEnabled(isEnabled);
    }

    private void addTag(String name) {
        Tag tag = tagAdapter.find(name);
        if (tag != null) {
            addTag(tag);
        } else {
            tag = new Tag();
            tag.setId(-1);
            tag.setName(name);
            addTag(tag);
        }
    }

    private void addTag(final Tag tag) {
        final ChipsatView chipView = new ChipsatView(this);
        chipView.setTag(tag);
        chipView.setText(tag.getName());
        chipView.setCloseIconVisible(true);
        chipView.setOnCloseIconClickListener(view -> removeTag(tag, chipView));
        chipView.setOnClickListener(view -> removeTag(tag, chipView));
        tagsView.addView(chipView);

        tagAdapter.set(tag, false);
        dismissTagDropDown();

        tagsView.setVisibility(View.VISIBLE);
    }

    private void removeTag(Tag tag, View view) {
        tagAdapter.set(tag, true);
        tagsView.removeView(view);

        // Workaround: Force notifyDataSetChanged
        tagsInput.setText(tagsInput.getText().toString());
        dismissTagDropDown();

        if (tagsView.getChildCount() == 0) {
            tagsView.setVisibility(View.GONE);
        }
    }

    private void dismissTagDropDown() {
        // Workaround
        tagsInput.post(() -> tagsInput.dismissDropDown());
    }

    private void showDialogCategories() {
        String[] categoryNames = new String[activeCategories.length];
        boolean[] visibleCategoriesOld = new boolean[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            type category = activeCategories[position];
            categoryNames[position] = getString(category.getStringResId());
            visibleCategoriesOld[position] = layoutMeasurements.hasCategory(category);
        }

        final boolean[] visibleCategories = visibleCategoriesOld.clone();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.categories)
            .setMultiChoiceItems(categoryNames, visibleCategoriesOld, (dialog, which, isChecked) -> visibleCategories[which] = isChecked)
            .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                for (int position = activeCategories.length - 1; position >= 0; position--) {
                    type category = activeCategories[position];
                    if (visibleCategories[position]) {
                        addMeasurementView(category);
                    } else {
                        removeMeasurementView(category);
                    }
                }
            })
            .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateDateTime() {
        buttonDate.setText(Helper.getDateFormat().print(time));
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void updateAlarm() {
        buttonAlarm.setText(alarmInMinutes > 0 ?
            String.format("%s %s",
                getString(R.string.alarm_reminder_in),
                choisirletimeUS.parseInterval(this, alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE)
            ) : getString(R.string.alarm_reminder_none));
    }

    private void addMeasurementView(type category) {
        scrollView.smoothScrollTo(0, 0);
        layoutMeasurements.addMeasurement(category);
    }

    private void removeMeasurementView(type category) {
        layoutMeasurements.removeMeasurement(category);
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        if (layoutMeasurements.getMeasurements().size() == 0) {
            // Allow entries with no measurements but with a note or tag
            if (StringUs.isBlank(editTextNotes.getText().toString()) && tagsView.getChildCount() == 0) {
                ViewUs.showSnackbar(rootView, getString(R.string.validator_value_none));
                inputIsValid = false;
            }
        } else {
            for (mesoration measurement : layoutMeasurements.getMeasurements()) {
                if (measurement == null) {
                    inputIsValid = false;
                }
            }
        }

        return inputIsValid;
    }

    @OnClick(R.id.fab)
    public void trySubmit() {
        toggleSubmitButton(false);

        // Convenience: Accept tag that hasn't been submitted by user
        String missingTag = tagsInput.getText().toString();
        if (!StringUs.isBlank(missingTag)) {
            addTag(missingTag);
            tagsInput.setText(null);
        }

        if (inputIsValid()) {
            submit();
        }

        toggleSubmitButton(true);
    }

    private void submit() {
        boolean isNewEntry = entry == null;
        DateTime originalDate = isNewEntry ? null : entry.getDate();
        if (isNewEntry) {
            entry = new Entry();
        }

        entry.setDate(time);
        entry.setNote(editTextNotes.length() > 0 ? editTextNotes.getText().toString() : null);
        entry = EntryDao.getInstance().createOrUpdate(entry);

        entry.getMeasurementCache().clear();
        for (type category : type.values()) {
            if (layoutMeasurements.hasCategory(category)) {
                mesoration measurement = layoutMeasurements.getMeasurement(category);
                measurement.setEntry(entry);
                //noinspection unchecked
                mesorationydk.getInstance(measurement.getClass()).createOrUpdate(measurement);
                entry.getMeasurementCache().add(measurement);
            } else {
                mesorationydk.getInstance(category.toClass()).deleteMeasurements(entry);
            }
        }

        // TODO: Delete distinct
        if (entryTags != null && entryTags.size() > 0) {
            EntryTagDao.getInstance().delete(entryTags);
        }

        List<Tag> tags = new ArrayList<>();
        List<EntryTag> entryTags = new ArrayList<>();
        for (int index = 0; index < tagsView.getChildCount(); index++) {
            View view = tagsView.getChildAt(index);
            if (view.getTag() instanceof Tag) {
                Tag tag = (Tag) view.getTag();
                if (tag.getId() < 0) {
                    tag = Tagydk.getInstance().createOrUpdate(tag);
                    Tag legacy = Tagydk.getInstance().getByName(tag.getName());
                    if (legacy != null) {
                        tag.setId(legacy.getId());
                    }
                }
                tag.setUpdatedAt(DateTime.now());
                tags.add(tag);

                EntryTag entryTag = new EntryTag();
                entryTag.setEntry(entry);
                entryTag.setTag(tag);
                entryTags.add(entryTag);
            }
        }
        Tagydk.getInstance().bulkCreateOrUpdate(tags);
        EntryTagDao.getInstance().bulkCreateOrUpdate(entryTags);

        List<FoodEaten> FoodEatenList = getFoodEaten();
        if (isNewEntry) {
            Toast.makeText(this, getString(R.string.entry_added), Toast.LENGTH_LONG).show();
            ets.post(new EntreAddedEt(entry, entryTags, FoodEatenList));
        } else {
            ets.post(new EntreUpdatedEt(entry, entryTags, originalDate, FoodEatenList));
        }

        if (alarmInMinutes > 0) {
            AlarmUS.setAlarm(alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE);
        }

        finish();
    }

    private void deleteEntry() {
        if (entry != null) {
            EntryDao.getInstance().delete(entry);
            ets.post(new EntreDeletedEt(entry, entryTags, getFoodEaten()));
            finish();
        }
    }

    private List<FoodEaten> getFoodEaten() {
        for (int index = 0; index < layoutMeasurements.getChildCount(); index++) {
            View view = layoutMeasurements.getChildAt(index);
            if (view instanceof mesorationView) {
                mesorationView measurementView = ((mesorationView) view);
                mesoration measurement = measurementView.getMeasurement();
                if (measurement instanceof Meal) {
                    return ((Meal) measurement).getFoodEatenCache();
                }
            }
        }
        return new ArrayList<>();
    }

    @OnClick(R.id.button_date)
    public void showDatePicker() {
        choisirladateFragment.newInstance(time, dateTime -> {
            if (dateTime != null) {
                time = dateTime;
                updateDateTime();
            }
        }).show(getSupportFragmentManager());
    }

    @OnClick(R.id.button_time)
    public void showTimePicker() {
        letempsPickerFragment.newInstance(time, (view, hourOfDay, minute) -> {
            time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
            updateDateTime();
        }).show(getSupportFragmentManager());
    }

    @OnClick(R.id.entry_button_alarm)
    public void showAlarmPicker() {
        ViewUs.showNumberPicker(this, R.string.minutes, alarmInMinutes, 0, 10000, (reference, number, decimal, isNegative, fullNumber) -> {
            alarmInMinutes = number.intValue();
            updateAlarm();
        });
    }

    @Override
    public void onCategorySelected(@Nullable type category) {
        addMeasurementView(category);
    }

    @Override
    public void onMiscellaneousSelected() {
        showDialogCategories();
    }

    @Override
    public void onCategoryAdded(type category) {
        fabMenu.ignore(category);
        fabMenu.restock();
    }

    @Override
    public void onCategoryRemoved(type category) {
        fabMenu.removeIgnore(category);
        fabMenu.restock();
    }
}