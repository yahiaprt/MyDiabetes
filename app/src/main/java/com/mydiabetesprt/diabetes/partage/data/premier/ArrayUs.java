package com.mydiabetesprt.diabetes.partage.data.premier;

/**
 * Created by Faltenreich on 18.10.2015.
 */
public class ArrayUs {

    public static float sum(float[] array) {
        float sum = 0f;
        for (float number : array) {
            sum += number;
        }
        return sum;
    }

    public static float avg(float[] avg) {
        return sum(avg) / avg.length;
    }
}
