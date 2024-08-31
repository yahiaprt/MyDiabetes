package com.yahia.healthysiabires.partage.data.serilisations;

import android.util.Log;

import androidx.annotation.Nullable;

import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;

import java.util.ArrayList;
import java.util.List;

public class typeSerialiser implements Serialiser<type[], String> {

    private static final String TAG = mesoration.class.getSimpleName();

    @Override
    @Nullable
    public String serialize(type[] categories) {
        if (categories != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (type category : categories) {
                stringBuilder.append(category.getStableId());
                if (category != categories[categories.length - 1]) {
                    stringBuilder.append(';');
                }
            }
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public type[] deserialize(String string) {
        if (string != null) {
            String[] ids = string.split(";");
            List<type> categories = new ArrayList<>();
            for (String id : ids) {
                try {
                    int stableId = Integer.parseInt(id);
                    type category = type.fromStableId(stableId);
                    if (category != null) {
                        categories.add(category);
                    }
                } catch (NumberFormatException exception) {
                    Log.e(TAG, exception.getMessage());
                }
            }
            return categories.toArray(new type[categories.size()]);
        } else {
            return null;
        }
    }
}
