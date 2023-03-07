package com.mydiabetesprt.diabetes.future.export.job.pdf.print;

import android.content.Context;
import android.util.Log;

import com.yahia.healthysiabires.R;import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.data.database.ydk.EntryDao;
import com.mydiabetesprt.diabetes.partage.data.database.entity.Entry;
import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.premier.FloatUs;
import com.mydiabetesprt.diabetes.future.datetemps.choisirletimeUS;
import com.mydiabetesprt.diabetes.future.export.job.pdf.meta.PdfExportCache;
import com.mydiabetesprt.diabetes.future.export.job.pdf.meta.PdfExportConfig;
import com.mydiabetesprt.diabetes.future.export.job.pdf.meta.PdfNote;
import com.mydiabetesprt.diabetes.future.export.job.pdf.meta.PdfNoteFactory;
import com.mydiabetesprt.diabetes.future.export.job.pdf.view.CellBuilder;
import com.mydiabetesprt.diabetes.future.export.job.pdf.view.CellFactory;
import com.mydiabetesprt.diabetes.future.export.job.pdf.view.SizedTable;
import com.mydiabetesprt.diabetes.future.timeline.jour.tablo.CategoryValueListItem;
import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Point;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PdfTable implements PdfPrintable {

    private static final String TAG = PdfTable.class.getSimpleName();
    private static final int HOURS_TO_SKIP = 2;

    private PdfExportCache cache;
    private SizedTable table;

    PdfTable(PdfExportCache cache) {
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

        List<Cell> cells = new ArrayList<>();
        Cell headerCell = new CellBuilder(new Cell(cache.getFontBold()))
            .setWidth(getLabelWidth())
            .setText(choisirletimeUS.toWeekDayAndDate(cache.getDateTime()))
            .build();
        cells.add(headerCell);

        float cellWidth = (cache.getPage().getWidth() - getLabelWidth()) / (DateTimeConstants.HOURS_PER_DAY / 2f);
        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += PdfTable.HOURS_TO_SKIP) {
            Cell hourCell = new CellBuilder(new Cell(cache.getFontNormal()))
                .setWidth(cellWidth)
                .setText(Integer.toString(hour))
                .setForegroundColor(Color.gray)
                .setTextAlignment(Align.CENTER)
                .build();
            cells.add(hourCell);
        }
        data.add(cells);

        LinkedHashMap<type, CategoryValueListItem[]> values = EntryDao.getInstance().getAverageDataTable(cache.getDateTime(), config.getCategories(), HOURS_TO_SKIP);
        int rowIndex = 0;
        for (type category : values.keySet()) {
            CategoryValueListItem[] items = values.get(category);
            if (items != null) {
                String label = context.getString(category.getStringAcronymResId());
                int backgroundColor = rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
                switch (category) {
                    case INSULIN:
                        if (config.isSplitInsulin()) {
                            data.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.bolus), backgroundColor));
                            data.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.correction), backgroundColor));
                            data.add(createMeasurementRows(cache, items, cellWidth, 2, label + " " + context.getString(R.string.basal), backgroundColor));
                        } else {
                            data.add(createMeasurementRows(cache, items, cellWidth, -1, label, backgroundColor));
                        }
                        break;
                    case PRESSURE:
                        data.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.systolic_acronym), backgroundColor));
                        data.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.diastolic_acronym), backgroundColor));
                        break;
                    default:
                        data.add(createMeasurementRows(cache, items, cellWidth, 0, label, backgroundColor));
                        break;
                }
                rowIndex++;
            }
        }

        if (config.isExportNotes() || config.isExportTags() || config.isExportmakla()) {
            List<PdfNote> pdfNotes = new ArrayList<>();
            for (Entry entry : EntryDao.getInstance().getEntriesOfDay(cache.getDateTime())) {
                PdfNote pdfNote = PdfNoteFactory.createNote(config, entry);
                if (pdfNote != null) {
                    pdfNotes.add(pdfNote);
                }
            }
            data.addAll(CellFactory.createRowsForNotes(cache, pdfNotes, getLabelWidth()));
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

    private List<Cell> createMeasurementRows(PdfExportCache cache, CategoryValueListItem[] items, float cellWidth, int valueIndex, String label, int backgroundColor) {
        List<Cell> cells = new ArrayList<>();

        Cell labelCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(getLabelWidth())
            .setText(label)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        cells.add(labelCell);

        for (CategoryValueListItem item : items) {
            type category = item.getCategory();
            float value = 0;
            switch (valueIndex) {
                case -1:
                    value = item.getValueTotal();
                    break;
                case 0:
                    value = item.getValueOne();
                    break;
                case 1:
                    value = item.getValueTwo();
                    break;
                case 2:
                    value = item.getValueThree();
                    break;
            }
            int textColor = Color.black;
            if (category == type.BLOODSUGAR && cache.getConfig().isHighlightLimits()) {
                if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    textColor = cache.getColorHyperglycemia();
                } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    textColor = cache.getColorHypoglycemia();
                }
            }
            float customValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? FloatUs.parseFloat(customValue) : "";

            Cell measurementCell = new CellBuilder(new Cell(cache.getFontNormal()))
                .setWidth(cellWidth)
                .setText(text)
                .setTextAlignment(Align.CENTER)
                .setBackgroundColor(backgroundColor)
                .setForegroundColor(textColor)
                .build();
            cells.add(measurementCell);
        }
        return cells;
    }
}
