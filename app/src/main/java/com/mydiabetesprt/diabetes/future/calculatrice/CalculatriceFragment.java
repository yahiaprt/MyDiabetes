package com.mydiabetesprt.diabetes.future.calculatrice;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;

import com.mydiabetesprt.diabetes.future.Entrer.editor.EntryEditActivity;
import com.mydiabetesprt.diabetes.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.FoodEatenydk;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.mesorationydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.glycemie;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Insulin;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Meal;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.data.EntreAddedEt;
import com.mydiabetesprt.diabetes.partage.event.preference.glycemiePreferenceChangedEt;
import com.mydiabetesprt.diabetes.partage.event.preference.CorrectionusinChangedEt;
import com.mydiabetesprt.diabetes.partage.event.preference.MealusinChangedEt;
import com.mydiabetesprt.diabetes.partage.event.preference.MealFactorUnitChangedEvent;
import com.mydiabetesprt.diabetes.partage.event.preference.UnitChangedEt;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.partage.data.validation.Validateur;
import com.mydiabetesprt.diabetes.partage.view.fragment.BaseFragment;
import com.mydiabetesprt.diabetes.future.makla.input.maklaInputView;
import com.mydiabetesprt.diabetes.future.navigation.MainButton;
import com.mydiabetesprt.diabetes.future.navigation.MainButtonProperties;
import com.mydiabetesprt.diabetes.partage.view.texteditor.StickHintInput;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 10.09.2016.
 */
public class CalculatriceFragment extends BaseFragment implements MainButton {

Boolean y=true;
    private InterstitialAd mInterstitialAd5;   private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;


    @BindView(R.id.calculator_bloodsugar)
    StickHintInput bloodSugarInput;
    @BindView(R.id.calculator_target)
    StickHintInput targetInput;
    @BindView(R.id.calculator_correction)
    StickHintInput correctionInput;
    @BindView(R.id.calculator_makla_list_view)
    maklaInputView maklaInputView;
    @BindView(R.id.calculator_factor)
    StickHintInput factorInput;
  CalculatriceFragment context0;
    public CalculatriceFragment() {
        super(R.layout.fragment_calculator, R.string.calculator);



//
        }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onBackButtonPressed() {

    }


    public  void showIntewrestial5() {
        if (mInterstitialAd5.isLoaded()) {
            mInterstitialAd5.show();
        } else {
            finish();
        }

    }




//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                // Code to be executed when the interstitial ad is closed.
//            }
//        });
//
//        mInterstitialAd2 = new InterstitialAd(getContext());
//        mInterstitialAd2.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd2.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                mInterstitialAd2.loadAd(new AdRequest.Builder().build());
//                // Code to be executed when the interstitial ad is closed.
//            }
//        });
//
//        mInterstitialAd3 = new InterstitialAd(getContext());
//        mInterstitialAd3.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd3.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd3.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                mInterstitialAd3.loadAd(new AdRequest.Builder().build());
//                // Code to be executed when the interstitial ad is closed.
//            }
//        });
//        mInterstitialAd4 = new InterstitialAd(getContext());
//        mInterstitialAd4.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd4.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd4.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                mInterstitialAd4.loadAd(new AdRequest.Builder().build());
//                // Code to be executed when the interstitial ad is closed.
//            }
//        });
//        mInterstitialAd.isLoaded();
//          mInterstitialAd.show();
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");}
//        if (mInterstitialAd2.isLoaded()) {
//            mInterstitialAd2.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");}
//
//        if (mInterstitialAd3.isLoaded()) {
//            mInterstitialAd3.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");}
//
//
//
//        if (mInterstitialAd4.isLoaded()) {
//            mInterstitialAd4.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");}
//














    @Override
    public void onResume() {
        super.onResume();
        ets.register(this);
        update();
    }

    @Override
    public void onDestroy() {
        ets.unregister(this);
        super.onDestroy();
    }

    private void update() {
        updateTargetValue();
        updateCorrectionValue();
        updateMealFactor();
    }

    private void clearInput() {
        bloodSugarInput.setText(null);
        targetInput.setText(null);
        correctionInput.setText(null);
        maklaInputView.clear();
        factorInput.setText(null);
    }

    private void updateTargetValue() {
        targetInput.setText(PreferenceHelper.getInstance().getMeasurementForUi(
            type.BLOODSUGAR,
            PreferenceHelper.getInstance().getTargetValue()));
    }

    private void updateCorrectionValue() {
        correctionInput.setText(PreferenceHelper.getInstance().getMeasurementForUi(
            type.BLOODSUGAR,
            PreferenceHelper.getInstance().getCorrectionForHour(
                DateTime.now().getHourOfDay())));
    }

    private void updateMealFactor() {
        int hourOfDay = DateTime.now().getHourOfDay();
        float factor = PreferenceHelper.getInstance().getFactorForHour(hourOfDay);
        factorInput.setText(factor >= 0 ? FloatUs.parseFloat(factor) : null);
        factorInput.setHint(getString(PreferenceHelper.getInstance().getFactorUnit().titleResId));
    }

    private boolean inputIsValid() {
        boolean isValid = true;

        // Blood Sugar
        if (!Validateur.validateEditTextEvent(getContext(), bloodSugarInput.getInputView(), type.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validateur.validateEditTextEvent(getContext(), targetInput.getInputView(), type.BLOODSUGAR, false)) {
            isValid = false;
        }
        if (!Validateur.validateEditTextEvent(getContext(), correctionInput.getInputView(), type.BLOODSUGAR, false)) {
            isValid = false;
        }

        // Meal
        if (maklaInputView.getTotalCarbohydrates() > 0) {
            // Factor
            if (!Validateur.validateEditTextFactor(getContext(), factorInput.getInputView(), false)) {
                isValid = false;
            } else {
                factorInput.setError(null);
            }
        }

        return isValid;
    }

    private float getBloodSugar() {
        return PreferenceHelper.getInstance().formatCustomToDefaultUnit(
            type.BLOODSUGAR,
            FloatUs.parseNumber(bloodSugarInput.getText()));
    }

    private float getTargetBloodSugar() {
        return Validateur.containsNumber(targetInput.getText()) ?
            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                type.BLOODSUGAR,
                FloatUs.parseNumber(targetInput.getText())) :
            PreferenceHelper.getInstance().getTargetValue();
    }

    private float getCorrectionFactor() {
        int hourOfDay = DateTime.now().getHourOfDay();
        return Validateur.containsNumber(correctionInput.getText()) ?
            PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                type.BLOODSUGAR,
                FloatUs.parseNumber(correctionInput.getText())) :
            PreferenceHelper.getInstance().getCorrectionForHour(hourOfDay);
    }

    private float getCarbohydrates() {
        return maklaInputView.getTotalCarbohydrates();
    }

    private float getMealFactor() {
        return Validateur.containsNumber(factorInput.getText()) ?
            FloatUs.parseNumber(factorInput.getText()) :
            FloatUs.parseNumber(factorInput.getHint());
    }

    private void calculate() {
        if (inputIsValid()) {

            float bloodSugar = getBloodSugar();
            float targetBloodSugar = getTargetBloodSugar();
            float correctionFactor = getCorrectionFactor();
            float insulinCorrection = (bloodSugar - targetBloodSugar) / correctionFactor;

            float carbohydrates = getCarbohydrates();
            float mealFactor = getMealFactor();
            float insulinBolus = carbohydrates * mealFactor * PreferenceHelper.getInstance().getFactorUnit().factor;

            StringBuilder builderFormula = new StringBuilder();
            StringBuilder builderFormulaContent = new StringBuilder();

            if (insulinBolus > 0) {
                String mealAcronym = PreferenceHelper.getInstance().getUnitAcronym(type.MEAL);
                String factorAcronym = getString(PreferenceHelper.getInstance().getFactorUnit().titleResId);
                builderFormula.append(String.format("%s * %s",
                    mealAcronym,
                    factorAcronym));
                builderFormula.append(" + ");

                builderFormulaContent.append(String.format("%s %s * %s",
                    PreferenceHelper.getInstance().getMeasurementForUi(type.MEAL, carbohydrates),
                    mealAcronym,
                    FloatUs.parseFloat(mealFactor)));
                builderFormulaContent.append(" + ");
            }

            builderFormula.append(String.format("(%s - %s) / %s",
                getString(R.string.bloodsugar),
                getString(R.string.pref_therapy_targets_target),
                getString(R.string.correction_value)));

            String bloodSugarUnit = PreferenceHelper.getInstance().getUnitAcronym(type.BLOODSUGAR);
            builderFormulaContent.append(String.format("(%s %s - %s %s) / %s %s",
                PreferenceHelper.getInstance().getMeasurementForUi(type.BLOODSUGAR, bloodSugar), bloodSugarUnit,
                PreferenceHelper.getInstance().getMeasurementForUi(type.BLOODSUGAR, targetBloodSugar), bloodSugarUnit,
                PreferenceHelper.getInstance().getMeasurementForUi(type.BLOODSUGAR, correctionFactor), bloodSugarUnit));

            builderFormula.append(String.format(" = %s", getString(R.string.bolus)));
            builderFormulaContent.append(String.format(" = %s %s",
                FloatUs.parseFloat(insulinBolus + insulinCorrection),
                PreferenceHelper.getInstance().getUnitAcronym(type.INSULIN)));

            showResult(builderFormula.toString(), builderFormulaContent.toString(), bloodSugar, carbohydrates, insulinBolus, insulinCorrection);
        }
    }

    // Values are normalized
    private void showResult(String formula, String formulaContent, final float bloodSugar, final float meal, final float bolus, final float correction) {
        float insulin = bolus + correction;

        // Build AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View viewPopup = inflater.inflate(R.layout.dialog_calculator_result, null);

        final ViewGroup infoLayout = viewPopup.findViewById(R.id.dialog_calculator_result_info);

        TextView textViewFormula = viewPopup.findViewById(R.id.dialog_calculator_result_formula);
        textViewFormula.setText(formula);

        TextView textViewFormulaContent = viewPopup.findViewById(R.id.dialog_calculator_result_formula_content);
        textViewFormulaContent.setText(formulaContent);

        // Handle negative insulin
        TextView textViewInfo = viewPopup.findViewById(R.id.textViewInfo);
        if (insulin <= 0) {
            // Advice skipping bolus
            viewPopup.findViewById(R.id.result).setVisibility(View.GONE);
            textViewInfo.setVisibility(View.VISIBLE);
            if (insulin < -1) {
                // Advice consuming carbohydrates
                textViewInfo.setText(String.format("%s %s", textViewInfo.getText().toString(), getString(R.string.bolus_no2)));
            }
        } else {
            viewPopup.findViewById(R.id.result).setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.GONE);
        }

        TextView textViewValue = viewPopup.findViewById(R.id.textViewResult);
        textViewValue.setText(FloatUs.parseFloat(insulin));

        TextView textViewUnit = viewPopup.findViewById(R.id.textViewUnit);
        textViewUnit.setText(PreferenceHelper.getInstance().getUnitAcronym(type.INSULIN));

        dialogBuilder.setView(viewPopup)
            .setTitle(R.string.bolus)
            .setNegativeButton(R.string.info, (dialog, id) -> { /* Set down below */ })
            .setPositiveButton(R.string.store_values, (dialog, id) -> storeValues(bloodSugar, meal, bolus, correction))
            .setNeutralButton(R.string.back, (dialog, id) -> dialog.cancel());

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> {
            infoLayout.setVisibility(View.VISIBLE);
            view.setEnabled(false);
        });
    }

    private void storeValues(float mgDl, float carbohydrates, float bolus, float correction) {
        DateTime now = DateTime.now();
        Entry entry = new Entry();
        entry.setDate(now);
        EntryDao.getInstance().createOrUpdate(entry);

        glycemie bloodSugar = new glycemie();
        bloodSugar.setMgDl(mgDl);
        bloodSugar.setEntry(entry);
        mesorationydk.getInstance(glycemie.class).createOrUpdate(bloodSugar);

        List<FoodEaten> FoodEatenList = new ArrayList<>();
        if (carbohydrates > 0) {
            FoodEatenList.addAll(maklaInputView.getFoodEatenList());
            Meal meal = new Meal();
            meal.setCarbohydrates(maklaInputView.getInputCarbohydrates());
            meal.setEntry(entry);
            mesorationydk.getInstance(Meal.class).createOrUpdate(meal);

            for (FoodEaten FoodEaten : FoodEatenList) {
                FoodEaten.setMeal(meal);
                FoodEatenydk.getInstance().createOrUpdate(FoodEaten);
            }
        }

        if (bolus > 0 || correction > 0) {
            Insulin insulin = new Insulin();
            insulin.setBolus(bolus);
            insulin.setCorrection(correction);
            insulin.setEntry(entry);
            mesorationydk.getInstance(Insulin.class).createOrUpdate(insulin);
        }

        ets.post(new EntreAddedEt(entry, null, FoodEatenList));

        openEntry(entry);
        clearInput();
        update();
    }

    private void openEntry(Entry entry) {
        EntryEditActivity.show(getContext(), entry);
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.confirmButton(v -> calculate(), false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(glycemiePreferenceChangedEt event) {
        if (isAdded()) {
            updateTargetValue();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CorrectionusinChangedEt event) {
        if (isAdded()) {
            updateCorrectionValue();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MealusinChangedEt event) {
        if (isAdded()) {
            updateMealFactor();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MealFactorUnitChangedEvent event) {
        if (isAdded()) {
            updateMealFactor();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEt event) {
        if (isAdded() && event.context == type.BLOODSUGAR) {
            updateTargetValue();
            updateCorrectionValue();
        }
    }
}
