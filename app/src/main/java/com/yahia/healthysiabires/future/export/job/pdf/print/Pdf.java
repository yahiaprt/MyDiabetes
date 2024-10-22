package com.yahia.healthysiabires.future.export.job.pdf.print;

import android.content.Context;

import com.yahia.healthysiabires.R;import com.yahia.healthysiabires.future.export.job.Export;
import com.yahia.healthysiabires.future.export.job.pdf.meta.PdfExportConfig;
import com.pdfjet.PDF;

import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;

public class Pdf extends PDF {

    public Pdf(File file, PdfExportConfig config) throws Exception {
        super(new FileOutputStream(file));
        init(config);
    }

    private void init(PdfExportConfig config) {
        Context context = config.getContext();
        DateTimeFormatter formatter = Export.getDateTimeFormatterForSubject();
        setCreator(context.getString(R.string.app_name));
        setTitle(String.format("%s %s",
            context.getString(R.string.app_name),
            context.getString(R.string.export))
        );
        // Reminder: Subject started with a prefix pre-3.3.0: Mydiabetes Export:
        setSubject(String.format("%s%s%s",
            formatter.print(config.getDateStart()),
            Export.EXPORT_SUBJECT_SEPARATOR,
            formatter.print(config.getDateEnd()))
        );
    }
}
