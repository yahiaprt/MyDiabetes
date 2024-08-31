package com.yahia.healthysiabires.future.makla.networkage;

 import com.yahia.healthysiabires.future.makla.networkage.ydk.SearchResponseydk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface OpenmaklaFactsApi {

    @GET("cgi/search.pl")
    Call<SearchResponseydk> search(
            @Query("search_terms") String query,
            @Query("json") int isJson,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );
}
