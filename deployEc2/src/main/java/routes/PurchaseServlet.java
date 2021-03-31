package routes;

import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PurchaseTransaction;
import model.Purchase;
import util.DateBuilder;
import util.PurchaseOrder;
import util.ResponseMessage;

@WebServlet(name = "routes.PurchaseServlet", value = "/routes.PurchaseServlet")
public class PurchaseServlet extends HttpServlet {
    private final static Gson GSON = new Gson();
    private final static String DIGIT_REGEX = "\\d+";
    private static final PurchaseTransaction DAO = new PurchaseTransaction();
    private static final ObjectMapper mapper = new ObjectMapper();
    private final boolean DEBUG = true;

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
                PurchaseOrder purchaseOrder = mapper.readValue(request.getReader().lines().collect(Collectors.joining()), PurchaseOrder.class);
                try {
                    // Save the object into the database
                    DAO.savePurchaseOrder(purchaseOrder.getItems().stream().map(item ->
                        new Purchase(
                            Integer.parseInt(urlParts[3]),
                            Integer.parseInt(urlParts[1]),
                            DateBuilder.getDate(urlParts[5]),
                            item.getNumberOfItems(),
                            item.getItemId())).collect(Collectors.toList()));
                    responseMessage.setMessage("It works! with save");
                } catch (Exception e) {
                    responseMessage.setMessage("ERROR: Save failed: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }

            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseMessage.setError("Url not valid");
            }
            response.getWriter().print(GSON.toJson(responseMessage));
            response.getWriter().flush();
        }
    }

    private boolean isPurchasePostUrlValid(String[] urlParts) {
        return DateBuilder.getDate(urlParts[5]) != null && urlParts[1].matches(DIGIT_REGEX) && urlParts[3].matches(DIGIT_REGEX);
    }
}
