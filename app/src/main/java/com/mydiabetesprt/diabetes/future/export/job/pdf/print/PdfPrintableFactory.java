package com.mydiabetesprt.diabetes.future.export.job.pdf.print;

import com.mydiabetesprt.diabetes.future.export.job.pdf.meta.PdfExportCache;
import com.mydiabetesprt.diabetes.future.export.job.pdf.meta.PdfExportStyle;

public class PdfPrintableFactory {

    public static PdfPrintable createPrintable(PdfExportCache cache) {
        PdfExportStyle style = cache.getConfig().getStyle();
        switch (style) {
            case TABLE:
                return new PdfTable(cache);
            case LOG:
                return new PdfLog(cache);
            case TIMELINE:
                return new PdfTimeline(cache);
            default:
                throw new IllegalArgumentException("Unsupported style: " + style);
        }
    }
}
