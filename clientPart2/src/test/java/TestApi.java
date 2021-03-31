import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import io.swagger.client.ApiException;
import io.swagger.client.api.PurchaseApi;
import io.swagger.client.model.Purchase;
import io.swagger.client.model.PurchaseItems;
import lombok.var;

import static org.junit.Assert.fail;

public class TestApi {
    private static PurchaseApi api;
    private static final int storeId = 1;
    private static final int numberOfCustomerPerStore = 1;
    private static final int maxItemId = 1000;
    private static String serverIpAddress;

    public static void main(String[] args) throws ApiException {
        var purchase = new Purchase();
        var itemsToPurchase = new PurchaseItems();
        itemsToPurchase.setNumberOfItems(5);
        itemsToPurchase.setItemID(String.valueOf(ThreadLocalRandom.current()
                                                         .nextInt(1, maxItemId)));
        purchase.addItemsItem(itemsToPurchase);
        var response = getApi().newPurchaseWithHttpInfo(
                purchase,
                storeId,
                ThreadLocalRandom.current()
                        .nextInt(storeId * 1000, storeId * 1000 + numberOfCustomerPerStore),
                "20200101");
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            System.out.println("Success");
        } else {
            System.out.println("Failure");
            System.err.println("Request failed" + response);
            throw new RuntimeException("Request failed to execute");
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

    @Test
    public void runCheck() {
        try {
            var properties = new Properties();
            var propertiesFileName = "config.properties";
            var inputStream =
                    ClientApp.class.getClassLoader().getResourceAsStream(propertiesFileName);
            if (inputStream == null) {
                throw new FileNotFoundException("Property file: " + propertiesFileName + " does " +
                                                        "not exist");
            } else {
                properties.load(inputStream);
            }
            serverIpAddress = properties.getProperty("serverIpAddress") == null
                              ? "bsds-1992801789.us-east-1.elb.amazonaws.com"
                              : properties.getProperty("serverIpAddress");
            System.out.println("Hitting on: " + getUrl());
            main(new String[]{});
        } catch (Exception e) {
            System.err.println("FAILURE REASON: " + e.getMessage());
            fail();
        }
    }
}
