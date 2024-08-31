package com.yahia.healthysiabires.future.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.SystemUs;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.ydk.Tagydk;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.fichier.restorationImportedEt;
import com.yahia.healthysiabires.partage.event.fichier.fichierProvidedEt;
import com.yahia.healthysiabires.partage.event.fichier.fichierProvidedFailedEt;
import com.yahia.healthysiabires.partage.event.permision.PermisionResponseEt;
import com.yahia.healthysiabires.partage.event.preference.MealFactorUnitChangedEvent;
import com.yahia.healthysiabires.partage.event.preference.UnitChangedEt;
import com.yahia.healthysiabires.partage.data.file.FileUs;
import com.yahia.healthysiabires.partage.data.permission.Permission;
import com.yahia.healthysiabires.partage.data.premier.FloatUs;
import com.yahia.healthysiabires.partage.data.synchronisation.DataLoader;
import com.yahia.healthysiabires.partage.data.synchronisation.DataLoaderListener;
import com.yahia.healthysiabires.partage.view.theme.Theme;
import com.yahia.healthysiabires.partage.view.theme.ThemeUs;
import com.yahia.healthysiabires.partage.view.activity.BaseActivity;
import com.yahia.healthysiabires.partage.view.progress.ProgressComponent;
import com.yahia.healthysiabires.future.export.job.Export;
import com.yahia.healthysiabires.future.export.job.ExportCallback;
import com.yahia.healthysiabires.future.preference.lesangusage.glucimiePreference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

// android.support.v7.preference has currently an incompatible API
@SuppressWarnings("deprecation")
public class PreferenceFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_OPENING_PREFERENCE = "EXTRA_OPENING_PREFERENCE";

    private ProgressComponent progressComponent = new ProgressComponent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        initPreferences();
        checkIntents();
    }

    @Override
    public void onResume() {
        super.onResume();
        ets.register(this);
        setSummaries();
    }

    @Override
    public void onDestroy() {
        ets.unregister(this);
        super.onDestroy();
    }

    private ArrayList<Preference> getPreferenceList(Preference preference, ArrayList<Preference> list) {
        if (preference instanceof PreferenceCategory || preference instanceof PreferenceScreen) {
            PreferenceGroup pGroup = (PreferenceGroup) preference;
            int pCount = pGroup.getPreferenceCount();
            for (int i = 0; i < pCount; i++) {
                getPreferenceList(pGroup.getPreference(i), list); // recursive call
            }
        } else {
            list.add(preference);
        }
        return list;
    }

    private void initPreferences() {
//        if (!BuildConfig.isCalculatorEnabled) {
            Preference categoryPreferenceLimits = findPreference("limits");
            if (categoryPreferenceLimits instanceof PreferenceCategory) {
                PreferenceCategory category = (PreferenceCategory) categoryPreferenceLimits;
                category.removePreference(findPreference("correction_value"));
                category.removePreference(findPreference("pref_factor"));
            }
            Preference categoryPreferenceUnits = findPreference("units");
            if (categoryPreferenceUnits instanceof PreferenceCategory) {
                PreferenceCategory category = (PreferenceCategory) categoryPreferenceUnits;
                category.removePreference(findPreference("unit_meal_factor"));
            }
        }


    private void setSummaries() {
        for (Preference preference : getPreferenceList(getPreferenceScreen(), new ArrayList<>())) {
            setSummary(preference);
        }
    }

    private void setSummary(final Preference preference) {
        if (isAdded() && preference != null) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());

            } else if (preference instanceof glucimiePreference) {
                String value = PreferenceHelper.getInstance().getValueForKey(preference.getKey());
                float number = FloatUs.parseNumber(value);
                if (number > 0) {
                    if (getActivity() != null) {
                        int descriptionResId = getResources().getIdentifier(preference.getKey() + "_desc", "string", getActivity().getPackageName());
                        String description = descriptionResId > 0 ? getString(descriptionResId) + " " : "";
                        number = PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.BLOODSUGAR, number);
                        value = FloatUs.parseFloat(number);
                        preference.setSummary(description + value + " " + PreferenceHelper.getInstance().getUnitAcronym(type.BLOODSUGAR));
                    }
                } else {
                    preference.setSummary(null);
                }

            } else if (preference.getKey() != null) {
                String key = preference.getKey();
                switch (key) {
                    case "version":
                        preference.setSummary(SystemUs.getVersionName(getActivity()));
                        break;
                    case "categories":
                        int activeCategoriesCount = PreferenceHelper.getInstance().getActiveCategories().length;
                        int categoriesTotalCount = type.values().length;
                        preference.setSummary(String.format("%d/%d %s",
                            activeCategoriesCount,
                            categoriesTotalCount,
                            getString(R.string.active)));
                        break;
                    case "tags":
                        DataLoader.getInstance().load(preference.getContext(), new DataLoaderListener<Long>() {
                            @Override
                            public Long onShouldLoad() {
                                return Tagydk.getInstance().countAll();
                            }

                            @Override
                            public void onDidLoad(Long count) {
                                if (isAdded()) {
                                    preference.setSummary(String.format(getString(R.string.available_placeholder), count));
                                }
                            }
                        });
                        break;
                }
            }
        }
    }

    private void checkIntents() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.getString(EXTRA_OPENING_PREFERENCE) != null) {
                preopenPreference(extras.getString(EXTRA_OPENING_PREFERENCE));
            }
        }
    }

    private void preopenPreference(String key) {
        int position = findPreferencePosition(key);
        if (position >= 0) {
            getPreferenceScreen().onItemClick(null, null, position, 0);
        }
    }

    private int findPreferencePosition(String key) {
        ListAdapter listAdapter = getPreferenceScreen().getRootAdapter();
        for (int position = 0; position < listAdapter.getCount(); position++) {
            if (listAdapter.getItem(position).equals(findPreference(key))) {
                return position;
            }
        }
        return -1;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.endsWith("_active")) {
            key = "categories";
        }
        switch (key) {
            case "unit_bloodsugar":
                ets.post(new UnitChangedEt(type.BLOODSUGAR));
                break;
            case "unit_meal":
                ets.post(new UnitChangedEt(type.MEAL));
                break;
            case "unit_meal_factor":
                ets.post(new MealFactorUnitChangedEvent());
                break;
            case "unit_hba1c":
                ets.post(new UnitChangedEt(type.HBA1C));
                break;
            case "unit_weight":
                ets.post(new UnitChangedEt(type.WEIGHT));
                break;
            case PreferenceHelper.Keys.THEME:
                Theme theme = PreferenceHelper.getInstance().getTheme();
                ThemeUs.setDefaultNightMode(theme);
                ThemeUs.setUiMode(getActivity(), theme);
                break;
        }
        setSummary(findPreference(key));
    }

    private void createBackup() {
        Context context = getActivity();
        progressComponent.show(context);

        ExportCallback callback = new ExportCallback() {
            @Override
            public void onProgress(String message) {
                progressComponent.setMessage(message);
            }
            @Override
            public void onSuccess(@Nullable File file, String mimeType) {
                progressComponent.dismiss();
                if (file != null) {
                    FileUs.shareFile(getActivity(), file, R.string.backup_store);
                } else {
                    onError();
                }
            }
            @Override
            public void onError() {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
            }
        };
        Export.exportCsv(context, callback);
    }

    private void importBackup(Uri uri) {
        progressComponent.show(getActivity());
        Export.importCsv(getActivity(), uri, new ExportCallback() {

            @Override
            public void onProgress(String message) {
                progressComponent.setMessage(message);
            }

            @Override
            public void onSuccess(@Nullable File file, String mimeType) {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getActivity().getString(R.string.backup_complete), Toast.LENGTH_SHORT).show();
                ets.post(new restorationImportedEt());
            }

            @Override
            public void onError() {
                progressComponent.dismiss();
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_import), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(fichierProvidedEt event) {
        importBackup(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(fichierProvidedFailedEt event) {
        Toast.makeText(getActivity(), getActivity().getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermisionResponseEt event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE && event.isGranted) {
            switch (event.useCase) {
                case BACKUP_WRITE:
                    createBackup();
                    break;
                case BACKUP_READ:
                    String mimeType = "text/*"; // Workaround: text/csv does not work for all apps
                    FileUs.searchFiles(getActivity(), mimeType, BaseActivity.REQUEST_CODE_BACKUP_IMPORT);
                    break;
            }
        }
    }
}