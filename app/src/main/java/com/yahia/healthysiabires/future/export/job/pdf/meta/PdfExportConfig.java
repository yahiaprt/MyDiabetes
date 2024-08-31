package com.yahia.healthysiabires.future.export.job.pdf.meta;

import android.content.Context;

import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.future.export.job.ExportCallback;
import com.yahia.healthysiabires.future.export.job.ExportConfig;
import com.yahia.healthysiabires.future.export.job.FileType;

import org.joda.time.DateTime;

public class PdfExportConfig extends ExportConfig {

    private final PdfExportStyle style;
    private final boolean exportHeader;
    private final boolean exportFooter;
    private final boolean exportNotes;
    private final boolean exportTags;
    private final boolean exportmakla;
    private final boolean splitInsulin;
    private final boolean highlightLimits;

    public PdfExportConfig(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        type[] categories,
        PdfExportStyle style,
        boolean exportHeader,
        boolean exportFooter,
        boolean exportNotes,
        boolean exportTags,
        boolean exportmakla,
        boolean splitInsulin,
        boolean highlightLimits
    ) {
        super(context, callback, dateStart, dateEnd, categories, FileType.PDF);
        this.style = style;
        this.exportHeader = exportHeader;
        this.exportFooter = exportFooter;
        this.exportNotes = exportNotes;
        this.exportTags = exportTags;
        this.exportmakla = exportmakla;
        this.splitInsulin = splitInsulin;
        this.highlightLimits = highlightLimits;
    }

    public PdfExportStyle getStyle() {
        return style;
    }

    public boolean isExportHeader() {
        return exportHeader;
    }

    public boolean isExportFooter() {
        return exportFooter;
    }

    public boolean isExportNotes() {
        return exportNotes;
    }

    public boolean isExportTags() {
        return exportTags;
    }

    public boolean isExportmakla() {
        return exportmakla;
    }

    public boolean isSplitInsulin() {
        return splitInsulin;
    }

    public boolean isHighlightLimits() {
        return highlightLimits;
    }

    @Override
    public void persistInSharedPreferences() {
        super.persistInSharedPreferences();
        PreferenceHelper.getInstance().setPdfExportStyle(style);
        PreferenceHelper.getInstance().setExportHeader(exportHeader);
        PreferenceHelper.getInstance().setExportFooter(exportFooter);
        PreferenceHelper.getInstance().setExportNotes(exportNotes);
        PreferenceHelper.getInstance().setExportTags(exportTags);
        PreferenceHelper.getInstance().setExportmakla(exportmakla);
        PreferenceHelper.getInstance().setExportInsulinSplit(splitInsulin);
        PreferenceHelper.getInstance().setLimitsAreHighlighted(highlightLimits);
    }
}
