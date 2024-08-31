package com.yahia.healthysiabires.future.export.job.pdf.print;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.partage.data.database.entity.mesoration;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryDao;
import com.yahia.healthysiabires.partage.data.database.ydk.EntryTagDao;
import com.yahia.healthysiabires.partage.data.database.ydk.FoodEatenydk;
import com.yahia.healthysiabires.partage.data.database.ydk.mesorationydk;
import com.yahia.healthysiabires.partage.data.database.entity.glycemie;
import com.yahia.healthysiabires.partage.data.database.entity.Entry;
import com.yahia.healthysiabires.partage.data.database.entity.EntryTag;
import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.data.database.entity.Meal;
import com.yahia.healthysiabires.partage.data.database.entity.Tag;
import com.yahia.healthysiabires.future.export.job.pdf.meta.PdfExportCache;
import com.yahia.healthysiabires.future.export.job.pdf.meta.PdfExportConfig;
import com.yahia.healthysiabires.future.export.job.pdf.view.CellBuilder;
import com.yahia.healthysiabires.future.export.job.pdf.view.CellFactory;
import com.yahia.healthysiabires.future.export.job.pdf.view.MultilineCell;
import com.yahia.healthysiabires.future.export.job.pdf.view.SizedTable;
import com.yahia.healthysiabires.future.datetemps.choisirletimeUS;
import com.yahia.healthysiabires.partage.data.premier.StringUs;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Point;

import java.util.ArrayList;
import java.util.List;

public class PdfLog implements PdfPrintable {

    private static final String TAG = PdfLog.class.getSimpleName();
    private static final float TIME_WIDTH = 72;

    private PdfExportCache cache;
    private SizedTable table;

    PdfLog(PdfExportCache cache) {
        this.cache = cache;
        this.table = new SizedTable();
        init();
    }

    @Override
    public float getHeight() {
        float height = table.getHeight();
        return height > 0 ? height + PdfPage.MARGIN : 0f;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        table.setLocation(position.getX(), position.getY());
        table.drawOn(page);
    }

    private void init() {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContext();

        List<List<Cell>> data = new ArrayList<>();
        List<Cell> headerRow = new ArrayList<>();
        Cell headerCell = new CellBuilder(new Cell(cache.getFontBold()))
            .setWidth(getLabelWidth())
            .setText(choisirletimeUS.toWeekDayAndDate(cache.getDateTime()))
            .build();
        headerRow.add(headerCell);
        data.add(headerRow);

        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        for (Entry entry : entries) {
            List<mesoration> measurements = EntryDao.getInstance().getMeasurements(entry, cache.getConfig().getCategories());
            entry.setMeasurementCache(measurements);
        }

        int rowIndex = 0;
        for (Entry entry : entries) {
            int backgroundColor = rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
            int oldSize = data.size();
            String time = entry.getDate().toString("HH:mm");

            for (mesoration measurement : entry.getMeasurementCache()) {
                type category = measurement.getCategory();
                int textColor = Color.black;
                if (category == type.BLOODSUGAR && config.isHighlightLimits()) {
                    glycemie bloodSugar = (glycemie) measurement;
                    float value = bloodSugar.getMgDl();
                    if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        textColor = cache.getColorHyperglycemia();
                    } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        textColor = cache.getColorHypoglycemia();
                    }
                }

                String measurementText = measurement.print(context);

                if (category == type.MEAL && config.isExportmakla()) {
                    List<String> maklaOfDay = new ArrayList<>();
                    Meal meal = (Meal) mesorationydk.getInstance(Meal.class).getMeasurement(entry);
                    if (meal != null) {
                        for (FoodEaten FoodEaten : FoodEatenydk.getInstance().getAll(meal)) {
                            String maklaNote = FoodEaten.print();
                            if (maklaNote != null) {
                                maklaOfDay.add(maklaNote);
                            }
                        }
                    }
                    if (!maklaOfDay.isEmpty()) {
                        String maklaText = TextUtils.join(", ", maklaOfDay);
                        measurementText = String.format("%s\n%s", measurementText, maklaText);
                    }
                }

                data.add(getRow(
                    cache,
                    data.size() == oldSize ? time : null,
                    context.getString(category.getStringAcronymResId()),
                    measurementText,
                    backgroundColor,
                    textColor
                ));
            }

            if (config.isExportTags()) {
                List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                if (!entryTags.isEmpty()) {
                    List<String> tagNames = new ArrayList<>();
                    for (EntryTag entryTag : entryTags) {
                        Tag tag = entryTag.getTag();
                        if (tag != null) {
                            String tagName = tag.getName();
                            if (!StringUs.isBlank(tagName)) {
                                tagNames.add(tagName);
                            }
                        }
                    }
                    data.add(getRow(
                        cache,
                        data.size() == oldSize ? time : null,
                        context.getString(R.string.tags),
                        TextUtils.join(", ", tagNames),
                        backgroundColor
                    ));
                }
            }

            if (config.isExportNotes()) {
                if (!StringUs.isBlank(entry.getNote())) {
                    data.add(getRow(
                        cache,
                        data.size() == oldSize ? time : null,
                        context.getString(R.string.note),
                        entry.getNote(),
                        backgroundColor
                    ));
                }
            }

            rowIndex++;
        }

        boolean hasData = data.size() > 1;
        if (!hasData) {
            data.add(CellFactory.createEmptyRow(cache));
        }

        try {
            table.setData(data);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private List<Cell> getRow(PdfExportCache cache, String title, String subtitle, String description, int backgroundColor, int foregroundColor) {
        List<Cell> entryRow = new ArrayList<>();
        float width = cache.getPage().getWidth();

        Cell titleCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(PdfLog.TIME_WIDTH)
            .setText(title)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        entryRow.add(titleCell);

        Cell subtitleCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(getLabelWidth())
            .setText(subtitle)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        entryRow.add(subtitleCell);

        Cell descriptionCell = new CellBuilder(new MultilineCell(cache.getFontNormal()))
            .setWidth(width - titleCell.getWidth() - subtitleCell.getWidth())
            .setText(description)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(foregroundColor)
            .build();
        entryRow.add(descriptionCell);

        return entryRow;
    }

    private List<Cell> getRow(PdfExportCache cache, String title, String subtitle, String description, int backgroundColor) {
        return getRow(cache, title, subtitle, description, backgroundColor, Color.black);
    }
}
