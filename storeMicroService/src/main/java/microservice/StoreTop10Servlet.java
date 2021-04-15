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

@WebServlet(name = "StoreServlet", value = "/StoreServlet")
public class StoreTop10Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<Integer, Map<String, Integer>> map = Store.getInventorySoldInStore();
        int storeId = Integer.parseInt(request.getPathInfo().substring(1));
        Map<String, Integer> inventorySold = map.getOrDefault(storeId, new HashMap<>());
        Top10ItemssResponseMessage result = new Top10ItemssResponseMessage();
        if (inventorySold.size() == 0) {
            System.out.println("No items sold at this store");
            result.setStores(new ArrayList<>());
        } else {
            List<Top10ItemsSold> top10Items =
                    inventorySold.entrySet().stream()
                            .map(entry -> new Top10ItemsSold(entry.getKey(),
                                                             entry.getValue()))
                            .sorted((o1, o2) -> o2.getTotalItemsSold() - o1.getTotalItemsSold())
                            .limit(10).collect(Collectors.toList());
            result.setStores(top10Items);
        }
        response.getWriter().print(Utility.getMapper().writeValueAsString(result));
        response.getWriter().flush();
    }
}
