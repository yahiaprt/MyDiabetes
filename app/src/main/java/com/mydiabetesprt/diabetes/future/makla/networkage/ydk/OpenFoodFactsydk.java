package com.mydiabetesprt.diabetes.future.makla.networkage.ydk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class OpenFoodFactsydk {

    @SerializedName("status")
    public Integer statusCode;

    @SerializedName("status_verbose")
    public String status;

    @SerializedName("code")
    public String code;
}
