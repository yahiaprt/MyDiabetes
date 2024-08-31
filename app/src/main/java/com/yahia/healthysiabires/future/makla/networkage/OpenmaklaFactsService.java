package com.yahia.healthysiabires.future.makla.networkage;

import android.util.Log;

import com.yahia.healthysiabires.partage.data.database.ydk.maklaydk;
import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.networkage.maklaSearchFailedEvent;
import com.yahia.healthysiabires.partage.event.networkage.maklaSearchSucceededEvent;
import com.yahia.healthysiabires.partage.networkage.NetworkageResponse;
import com.yahia.healthysiabires.partage.networkage.NetworkageService;
import com.yahia.healthysiabires.future.makla.networkage.ydk.SearchResponseydk;

import java.util.ArrayList;
import java.util.List;

public class OpenmaklaFactsService extends NetworkageService<OpenmaklaFactsServer> {

    private static final String TAG = OpenmaklaFactsService.class.getSimpleName();
    private static final int JSON = 1;
    private static final int PAGE_SIZE = 50;

    private static OpenmaklaFactsService instance;

    public static OpenmaklaFactsService getInstance() {
        if (instance == null) {
            instance = new OpenmaklaFactsService();
        }
        return instance;
    }

    private OpenmaklaFactsService() {
        super(new OpenmaklaFactsServer());
    }

    public void search(final String query, final int page) {
        // Paging of this api starts at page 1
        execute(server.api.search(query != null ? query : "", JSON, page + 1, PAGE_SIZE), new NetworkageService.NetworkResponseListener<SearchResponseydk>() {
            @Override
            public void onResponse(NetworkageResponse<SearchResponseydk> response) {
                SearchResponseydk ydk = response.getData();
                if (ydk != null && ydk.products.size() > 0) {
                    Log.d(TAG, String.format("Received %d products for '%s' on page %d (%d total)", ydk.products.size(), query, page, ydk.count));
                    List<Food> maklaList = maklaydk.getInstance().createOrUpdate(ydk);
                    ets.post(new maklaSearchSucceededEvent(maklaList));
                } else {
                    ets.post(new maklaSearchSucceededEvent(new ArrayList<>()));
                }
            }
            @Override
            public void onError(NetworkageResponse<SearchResponseydk> response) {
                ets.post(new maklaSearchFailedEvent());
            }
        });
    }
}
