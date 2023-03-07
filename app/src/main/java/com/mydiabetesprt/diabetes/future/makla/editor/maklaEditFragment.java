package com.mydiabetesprt.diabetes.future.makla.editor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.InterstitialAd;
import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.ydk.maklaydk;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.data.maklaSavedEt;
import com.mydiabetesprt.diabetes.future.makla.BaseFoodFragment;
import com.mydiabetesprt.diabetes.partage.view.texteditor.StickHintInput;
import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.view.ViewUs;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by Faltenreich on 01.11.2016.
 */

public class maklaEditFragment extends BaseFoodFragment {
    Boolean y=true;

    final maklaEditFragment context0=this;
    private InterstitialAd mInterstitialAd;   private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;
    private InterstitialAd mInterstitialAd4;

    @BindView(R.id.makla_edit_name)
    StickHintInput nameInput;
    @BindView(R.id.makla_edit_brand)
    StickHintInput brandInput;
    @BindView(R.id.makla_edit_ingredients)
    StickHintInput ingredientsInput;
    @BindView(R.id.makla_edit_nutrition_input_layout)
    nutritionInputLayout nutritionInputLayout;

    public maklaEditFragment() {
        super(R.layout.fragment_food_edit, R.string.makla_new, R.menu.form_edit);

       //        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//         mInterstitialAd = new InterstitialAd(getContext());
//        mInterstitialAd.setAdUnitId("ca-app-pub-3808047780782051/4043570060");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                finish();
//            }
//
//        });
//
//        }
//
//    @Override
//    public void onBackPressed() {
//        showIntewrestial();
//
//    }
//    @Override
//    public void onBackButtonPressed() {
//        if (y=true) {
//            showIntewrestial();
//        } else {
//            getActivity().onBackPressed();
//        }
//    }
//
//    public  void showIntewrestial(){
//        if(mInterstitialAd.isLoaded()){
//            mInterstitialAd.show();
//        }else {finish();
//        }

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onBackButtonPressed() {

    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle(getTitle());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_delete).setVisible(getmakla() != null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deletemaklaIfConfirmed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public String getTitle() {
        return getString(getmakla() != null ? R.string.makla_edit : R.string.makla_new);
    }

    private void init() {
        Food makla = getmakla();
        if (makla != null) {
            nameInput.setText(makla.getName());
            brandInput.setText(makla.getBrand());
            ingredientsInput.setText(makla.getIngredients());
        }
        for (Food.nutrition nutrition : Food.nutrition.values()) {
            nutritionInputLayout.addnutrition(nutrition, makla != null ? nutrition.getValue(makla) : null);
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (nameInput.getText().length() == 0) {
            nameInput.setError(getString(R.string.validator_value_empty));
            isValid = false;
        }

        // Check for carbohydrates
        nutritionInputView carbohydratesInputView = nutritionInputLayout.getInputView(Food.nutrition.CARBOHYDRATES);
        if (carbohydratesInputView == null) {
            ViewUs.showSnackbar(getView(), getString(R.string.validator_value_empty_carbohydrates));
            isValid = false;
        } else  {
            Float value = carbohydratesInputView.getValue();
            if (value == null || value < 0) {
                carbohydratesInputView.setError(getString(R.string.validator_value_empty));
                isValid = false;
            }
        }
        return isValid;
    }

    @OnClick(R.id.fab)
    public void store() {
        if (isValid()) {
            Food makla = getmakla();
            if (makla == null) {
                makla = new Food();
            }
            makla.setLanguageCode(Helper.getLanguageCode());
            makla.setName(nameInput.getText());
            makla.setBrand(brandInput.getText());
            makla.setIngredients(ingredientsInput.getText());

            for (Map.Entry<Food.nutrition, Float> entry : nutritionInputLayout.getValues().entrySet()) {
                Food.nutrition nutrition = entry.getKey();
                Float value = entry.getValue();
                nutrition.setValue(makla, value != null ? value : -1);
            }

            maklaydk.getInstance().createOrUpdate(makla);
            ets.post(new maklaSavedEt(makla));

            finish();
        }
    }
}
