package com.mydiabetesprt.diabetes.partage.data.database.ydk;

/**
 * Created by Faltenreich on 03.04.2016.
 */
public enum SqlFunction {
    AVG("avg"),
    SUM("sum");

    public String function;

    SqlFunction(String function) {
        this.function = function;
    }
}
