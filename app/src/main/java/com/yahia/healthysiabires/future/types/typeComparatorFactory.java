package com.yahia.healthysiabires.future.types;

import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.entity.mesoration;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class typeComparatorFactory {

    private static typeComparatorFactory instance;

    public static typeComparatorFactory getInstance() {
        if (instance == null) {
            instance = new typeComparatorFactory();
        }
        return instance;
    }

    private HashMap<type, Integer> sortCache;

    private typeComparatorFactory() {

    }

    public void invalidate() {
        sortCache = new HashMap<>();
        Comparator<type> comparator = (first, second) -> PreferenceHelper.getInstance().getCategorySortIndex(first) - PreferenceHelper.getInstance().getCategorySortIndex(second);
        List<type> categories = PreferenceHelper.getInstance().getSortedCategories(comparator);
        for (int sortIndex = 0; sortIndex < categories.size(); sortIndex++) {
            type category = categories.get(sortIndex);
            sortCache.put(category, sortIndex);
        }
    }

    private HashMap<type, Integer> getSortCache() {
        if (sortCache == null) {
            invalidate();
        }
        return sortCache;
    }

    private int getSortIndex(type category) {
        HashMap<type, Integer> cache = getSortCache();
        Integer sortIndex = cache != null && cache.containsKey(category) ? cache.get(category) : null;
        return sortIndex != null ? sortIndex : 0;
    }

    public Comparator<type> createComparatorFromCategories() {
        return (first, second) -> getSortIndex(first) - getSortIndex(second);
    }

    public Comparator<mesoration> createComparatorFromMeasurements() {
        return (first, second) -> getSortIndex(first.getCategory()) - getSortIndex(second.getCategory());
    }
}
