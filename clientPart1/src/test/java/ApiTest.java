import com.google.gson.Gson;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.PurchaseApi;
import io.swagger.client.model.Purchase;

public class ApiTest {
    public static void main(String[] args) {

        PurchaseApi apiInstance = new PurchaseApi();
//        apiInstance.getApiClient().setBasePath("http://54.88.175.150:8080/deploy/");
        apiInstance.getApiClient().setBasePath("http://localhost:8080/deployEc2_war_exploded/");
        Purchase body = new Purchase(); // Purchase | items purchased
        Integer storeID = 56; // Integer | ID of the store the purchase takes place at
        Integer custID = 56; // Integer | customer ID making purchase
        String date = "20210101"; // String | date of purchase
        try {
            ApiResponse<Void> x = apiInstance.newPurchaseWithHttpInfo(body, storeID, custID, date);
            System.out.println(x.getData());;
            System.out.println(x.getStatusCode());
            System.out.println(new Gson().toJson(x));
        } catch (ApiException e) {
            System.err.println("Exception when calling PurchaseApi#newPurchase");
            e.printStackTrace();
        }
    }
}
