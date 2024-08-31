package com.yahia.healthysiabires.future.export.job.pdf.print;

import com.pdfjet.Point;

public interface PdfPrintable {

    float getHeight();

    default float getLabelWidth() {
        return 100;
    }

    void drawOn(PdfPage page, Point position) throws Exception;
}
