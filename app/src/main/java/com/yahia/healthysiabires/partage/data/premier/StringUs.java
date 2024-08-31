package com.yahia.healthysiabires.partage.data.premier;

import androidx.annotation.NonNull;

public class StringUs {

    public static boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
    }

    @NonNull
    public static String newLine() {
        String system = System.getProperty("line.separator");
        return system != null ? system : "\n";
    }
}
