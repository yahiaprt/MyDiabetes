package  com.yahia.healthysiabires.future.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.common.collect.ImmutableList;
import com.yahia.healthysiabires.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.android.billingclient.api.BillingClient.SkuType.SUBS;
public class billings extends AppCompatActivity implements PurchasesUpdatedListener {
     private Context context = this;
    private Button button;
    private BillingClient billingClient;
ProductDetails productDetails;

    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billings);

        context = this;

        // Initialize BillingClient
        billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener) // Set the listener to the current activity
                .enablePendingPurchases()
                .build();
        startBillingClientConnection();

        // Connect to Google Play
        button = findViewById(R.id.subscribe);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePurchase();
            }
        });
    }

    private void startBillingClientConnection() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(context, "Billing client setup successful", Toast.LENGTH_SHORT).show();
                    // BillingClient is ready. You can query purchases or start the purchase flow here.
                    querySkuDetails();
                } else {
                    Toast.makeText(context, "Billing client setup failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to Google Play
                Toast.makeText(context, "Billing client disconnected", Toast.LENGTH_SHORT).show();
                startBillingClientConnection();
            }
        });
    }

    private void querySkuDetails() {
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                .setProductId("dia_test")
                .setProductType(BillingClient.ProductType.SUBS)
                .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();
        Toast.makeText(context, "Subscription details loaded", Toast.LENGTH_SHORT).show();

        billingClient.queryProductDetailsAsync(
                params,
                (billingResult, productDetailsList) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

              productDetails =  productDetailsList.get(0);
                        Toast.makeText(context, "Subscription details loaded", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Failed to load subscription details", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    public void initiatePurchase() {
         int selectedOfferIndex = 0;
        String offerToken = productDetails
                .getSubscriptionOfferDetails()
                .get(selectedOfferIndex)
                .getOfferToken();

// Set the parameters for the offer that will be presented
// in the billing flow creating separate productDetailsParamsList variable
        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(offerToken)
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

// Launch the billing flow
        BillingResult billingResult = billingClient.launchBillingFlow(this, billingFlowParams);

        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            Toast.makeText(context, "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                // Handle the purchased subscription
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(context, "Purchase cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Purchase failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePurchase(Purchase purchase) {
        // Verify the purchase and acknowledge it
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
            // Perform verification and deliver the content to the user

            // Acknowledge the purchase
            AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();

            billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                @Override
                public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Toast.makeText(context, "Purchase acknowledged", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Purchase not acknowledged", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billingClient != null) {
            billingClient.endConnection();
        }
    }
}
