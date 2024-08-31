package com.yahia.healthysiabires.future.makla.networkage.ydk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponseydk {

    @SerializedName("count")
    public int count;

    @SerializedName("products")
    public List<Productydk> products;
}
