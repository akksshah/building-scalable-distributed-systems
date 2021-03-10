import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.swagger.client.ApiException;
import io.swagger.client.api.PurchaseApi;
import io.swagger.client.model.Purchase;
import io.swagger.client.model.PurchaseItems;

public class TestApi {
     private static final String serverIpAddress = "bsds-1992801789.us-east-1.elb.amazonaws.com";
//private static final String serverIpAddress = "3.91.241.111";
//    private static final String serverIpAddress = "localhost";
    private static final String serverPort = "8080";
    private static PurchaseApi api;
    private static final int numPurchases = 1;
    private static final int numberOfHoursOperational = 1;
    private static final int numberOfItemsToPurchasePerOrder = 1;
    private static final int storeId = 1;
    private static final int numberOfCustomerPerStore = 1;
    private static final int maxItemId = 1000;
    private static final List<RequestTracker> requestTracker = new ArrayList<>();
    public static void main(String[] args) {
        var purchase = new Purchase();
        var itemsToPurchase = new PurchaseItems();
        itemsToPurchase.setNumberOfItems(5);
        itemsToPurchase.setItemID(String.valueOf(ThreadLocalRandom.current().nextInt(1, maxItemId)));
        purchase.addItemsItem(itemsToPurchase);
        var requestInfoTracker = new RequestTracker();
        try {
            requestInfoTracker.setRequestType(RequestType.POST);
            requestInfoTracker.setStartTime(System.currentTimeMillis());
            var response = getApi().newPurchaseWithHttpInfo(
                purchase,
                storeId,
                ThreadLocalRandom.current().nextInt(storeId * 1000, storeId * 1000 + numberOfCustomerPerStore),
                "20200101");
            requestInfoTracker.setResponseCode(response.getStatusCode());
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                System.out.println("Success");
                //counter.requestSucceeded();
                // System.out.println(counter.getTotalRequestSent() + " Request sent, this was successful");
            } else {
                System.out.println("Failure");
                //counter.requestFailed();
                System.err.println("Request failed" + response);
            }
        } catch (ApiException e) {
            System.err.println("Request failed due to: " + e.getMessage());
            requestInfoTracker.setResponseCode(null);
            // counter.requestFailed();
        } finally {
            requestInfoTracker.setEndTime(System.currentTimeMillis());
        }
    }
    private static String getUrl() {
        return serverIpAddress.equals("localhost")
                   ? "http://localhost:8080/deployEc2_war_exploded/"
                   : "http://" + serverIpAddress + "/deployEc2/";
    }

    private static PurchaseApi getApi() {
        if (api == null) {
            api = new PurchaseApi();
            api.getApiClient().setBasePath(getUrl());
        }
        return api;
    }
}
