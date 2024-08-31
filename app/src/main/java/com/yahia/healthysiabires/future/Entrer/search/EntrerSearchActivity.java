package com.yahia.healthysiabires.future.Entrer.search;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.partage.view.activity.BaseActivity;
import com.yahia.healthysiabires.partage.view.activity.BaseSearchActivity;

public class EntrerSearchActivity extends BaseSearchActivity {

    private static Intent getIntent(Context context, @Nullable View source) {
        return BaseActivity.getIntent(EntrerSearchActivity.class, context, source);
    }

    public static void show(Context context, @Nullable View source) {
        context.startActivity(getIntent(context, source));
    }

    public static void show(Context context, Tag tag) {
        Intent intent = getIntent(context, null);
        intent.putExtra(EntrerSearchFragment.EXTRA_TAG_ID, tag.getId());
        context.startActivity(intent);
    }

    public EntrerSearchActivity() {
        super(R.layout.activity_entry_search);
    }
}