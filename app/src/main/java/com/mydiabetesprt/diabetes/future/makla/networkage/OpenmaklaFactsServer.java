package com.mydiabetesprt.diabetes.future.makla.networkage;

import com.mydiabetesprt.diabetes.partage.Helper;
import com.mydiabetesprt.diabetes.partage.networkage.NetworkageServer;

public class OpenmaklaFactsServer extends NetworkageServer<OpenmaklaFactsApi> {

    OpenmaklaFactsServer() {
        super(OpenmaklaFactsApi.class);
    }

    @Override
    public String getBaseUrl() {
        String languageCode = Helper.getLanguageCode();
        // Special case: "en" must be stripped for api to work
        if (languageCode.equals("en")) {
            return "https://world.OpenFoodFacts.org/";
        } else {
            return String.format("https://world-%s.OpenFoodFacts.org/", languageCode);
        }
    }
}
