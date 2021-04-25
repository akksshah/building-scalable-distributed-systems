package routes;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Order;
import util.DateBuilder;
import util.PurchaseOrder;
import util.PurchasedItems;
import util.ResponseMessage;
import util.Utility;

@WebServlet(name = "routes.PurchaseServlet", value = "/routes.PurchaseServlet")
public class PurchaseServlet extends HttpServlet {
    private final static String DIGIT_REGEX = "\\d+";
    public static DynamoDBMapper mapper =
            new DynamoDBMapper(AmazonDynamoDBClient.builder().withRegion(Regions.US_EAST_1).build());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        throw new ServletException("Get not allowed");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseMessage responseMessage = new ResponseMessage();

        String urlPath = request.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseMessage.setError("Url not found");
        } else {
            String[] urlParts = urlPath.split("/");
            // and now validate url path and return the response status code
            // (and maybe also some value if input is valid)
            if (isPurchasePostUrlValid(urlParts)) {
                response.setStatus(HttpServletResponse.SC_OK);
                // Convert Json to the purchaseOrder object that we need to insert into the database
                PurchaseOrder purchaseOrder =
                        Utility.getMapper().readValue(request.getReader().lines().collect(Collectors.joining()), PurchaseOrder.class);
                System.out.println(Utility.getMapper().writeValueAsString(purchaseOrder));
                try {
                    Order order = new Order();
                    order.setItems(purchaseOrder
                                           .getItems()
                                           .stream()
                                           .map(i ->
                                                        new PurchasedItems(i.getItemId(),
                                                                           i.getNumberOfItems())).collect(Collectors.toList()));
                    order.setCustomerId(Integer.parseInt(urlParts[3]));
                    order.setStoreId(Integer.parseInt(urlParts[1]));
                    order.setDate(DateBuilder.getDate(urlParts[5]));
                    int count = 1;
                    while (true) {
                        try {
                            mapper.save(order);
                            if (count != 1) {
                                System.err.println("Request succeeded after tries: " + count);
                            }
                            break;
                        } catch (Exception ignored) { count++; }
                    }
                    responseMessage.setMessage("It works! with save");
                } catch (Exception e) {
                    responseMessage.setMessage("ERROR: Save failed: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseMessage.setError("Url not valid");
            }
            response.getWriter().print(Utility.getMapper().writeValueAsString(responseMessage));
            response.getWriter().flush();
        }
    }

    private boolean isPurchasePostUrlValid(String[] urlParts) {
        return DateBuilder.getDate(urlParts[5]) != null && urlParts[1].matches(DIGIT_REGEX) && urlParts[3].matches(DIGIT_REGEX);
    }
}
