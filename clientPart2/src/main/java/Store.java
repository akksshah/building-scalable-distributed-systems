import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import io.swagger.client.ApiException;
import io.swagger.client.api.PurchaseApi;
import io.swagger.client.model.Purchase;
import io.swagger.client.model.PurchaseItems;
import lombok.var;

public class Store implements Runnable {
    private final String serverIpAddress;
    private final String serverPort;
    private PurchaseApi api;
    private final RequestCounter counter;
    private final CountDownLatch latch;
    private final int numPurchases;
    private final int numberOfHoursOperational;
    private final int numberOfItemsToPurchasePerOrder;
    private final int storeId;
    private final int numberOfCustomerPerStore;
    private final int maxItemId;
    private final List<RequestTracker> requestTracker;

    public Store(String serverIpAddress, String serverPort, int numPurchases, int numberOfItemsToPurchasePerOrder, int storeId,
                 int numberOfCustomerPerStore, int maxItemId, int numberOfHoursOperational, RequestCounter counter, CountDownLatch latch,
                 List<RequestTracker> requestTracker) {
        this.serverIpAddress = serverIpAddress;
        this.serverPort = serverPort;
        this.numPurchases = numPurchases;
        this.numberOfItemsToPurchasePerOrder = numberOfItemsToPurchasePerOrder;
        this.storeId = storeId;
        this.numberOfCustomerPerStore = numberOfCustomerPerStore;
        this.maxItemId = maxItemId;
        this.numberOfHoursOperational = numberOfHoursOperational;
        this.counter = counter;
        this.latch = latch;
        this.requestTracker = requestTracker;
    }

    @Override
    public void run() {
        IntStream.range(0, numPurchases * numberOfHoursOperational).forEach(i -> makeRequest());
    }

    private void makeRequest() {
        var purchase = new Purchase();
        var itemsToPurchase = new PurchaseItems();
        itemsToPurchase.setNumberOfItems(numberOfItemsToPurchasePerOrder);
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
                counter.requestSucceeded();
            } else {
                counter.requestFailed();
                System.err.println("Request failed" + response);
            }
        } catch (ApiException e) {
            System.err.println("Request failed due to: " + e.getMessage());
            requestInfoTracker.setResponseCode(null);
            counter.requestFailed();
        } finally {
            requestInfoTracker.setEndTime(System.currentTimeMillis());
        }
        requestTracker.add(requestInfoTracker);
        latch.countDown();
    }

    private String getUrl() {
        return serverIpAddress.equals("localhost")
                   ? "http://localhost:8080/deployEc2_war_exploded/"
                   : "http://" + serverIpAddress + "/deployEc2/";
    }

    private PurchaseApi getApi() {
        if (api == null) {
            api = new PurchaseApi();
            api.getApiClient().setBasePath(getUrl());
        }
        return api;
    }
}
