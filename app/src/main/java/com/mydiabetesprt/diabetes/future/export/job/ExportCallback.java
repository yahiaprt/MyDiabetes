package com.mydiabetesprt.diabetes.future.export.job;

import androidx.annotation.Nullable;

import java.io.File;

public interface ExportCallback {
    void onProgress(String message);
    void onSuccess(@Nullable File file, String mimeType);
    void onError();
}
