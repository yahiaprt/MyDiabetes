package com.yahia.healthysiabires.future.export.job.pdf.meta;

import org.joda.time.DateTime;

public class PdfNote {

    private DateTime dateTime;
    private String note;

    public PdfNote(DateTime dateTime, String note) {
        this.dateTime = dateTime;
        this.note = note;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getNote() {
        return note;
    }
}
