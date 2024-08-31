package com.yahia.healthysiabires.future.export.job.pdf.meta;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryTagDao;
import com.yahia.healthysiabires.partage.data.database.ydk.FoodEatenydk;
import com.yahia.healthysiabires.partage.data.database.ydk.mesorationydk;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.partage.data.premier.StringUs;

import java.util.ArrayList;
import java.util.List;

public class PdfNoteFactory {

    @Nullable
    public static PdfNote createNote(PdfExportConfig config, Entry entry) {
        List<String> entryNotesAndTagsOfDay = new ArrayList<>();
        List<String> maklaOfDay = new ArrayList<>();
        if (config.isExportNotes() && !StringUs.isBlank(entry.getNote())) {
            entryNotesAndTagsOfDay.add(entry.getNote());
        }
        if (config.isExportTags()) {
            List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
            for (EntryTag entryTag : entryTags) {
                Tag tag = entryTag.getTag();
                if (tag != null) {
                    entryNotesAndTagsOfDay.add(entryTag.getTag().getName());
                }
            }
        }
        if (config.isExportmakla()) {
            Meal meal = (Meal) mesorationydk.getInstance(Meal.class).getMeasurement(entry);
            if (meal != null) {
                for (FoodEaten FoodEaten : FoodEatenydk.getInstance().getAll(meal)) {
                    String maklaNote = FoodEaten.print();
                    if (maklaNote != null) {
                        maklaOfDay.add(maklaNote);
                    }
                }
            }
        }
        boolean hasEntryNotesAndTags = !entryNotesAndTagsOfDay.isEmpty();
        boolean hasmakla = !maklaOfDay.isEmpty();
        if (hasEntryNotesAndTags || hasmakla) {
            List<String> notes = new ArrayList<>();
            if (hasEntryNotesAndTags) {
                notes.add(TextUtils.join(", ", entryNotesAndTagsOfDay));
            }
            if (hasmakla) {
                notes.add(TextUtils.join(", ", maklaOfDay));
            }
            String note = TextUtils.join("\n", notes);
            return new PdfNote(entry.getDate(), note);
        }
        return null;
    }
}
