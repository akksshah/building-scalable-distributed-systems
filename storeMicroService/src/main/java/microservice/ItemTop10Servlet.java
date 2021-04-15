package microservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Utility;

@WebServlet(name = "ItemTop10Servlet", value = "/ItemTop10Servlet")
public class ItemTop10Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Map<Integer, Integer>> map = Store.getInventorySold();
        String itemId = request.getPathInfo().substring(1);
        Map<Integer, Integer> inventorySold = map.getOrDefault(itemId, new HashMap<>());
        Top10ItemsResponseMessage result = new Top10ItemsResponseMessage();
        if (inventorySold.size() == 0) {
            System.out.println("No items sold at this store");
            result.setStores(new ArrayList<>());
        } else {
            List<Top10Stores> top10Stores =
                    inventorySold.entrySet().stream()
                            .map(entry -> new Top10Stores(entry.getKey(), entry.getValue()))
                            .sorted((o1, o2) -> o2.getTotalItemsSold() - o1.getTotalItemsSold())
                            .collect(Collectors.toList());
            result.setStores(top10Stores.subList(0, 5));
        }
        response.getWriter().print(Utility.getMapper().writeValueAsString(result));
        response.getWriter().flush();
    }
}
