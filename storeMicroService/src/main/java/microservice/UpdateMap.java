package microservice;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import util.Order;

@AllArgsConstructor
public class UpdateMap implements Runnable {
    private final Order purchaseOrder;
    private final Map<Integer, Map<String, Integer>> storeInventory;
    private final Map<String, Map<Integer, Integer>> itemInventory;
    @Override
    public void run() {
        Map<String, Integer> purchaseInventories =
                storeInventory.getOrDefault(purchaseOrder.getStoreId(), new HashMap<>());
        purchaseOrder.getItems()
                .forEach(item -> purchaseInventories.put(item.getItemId(),
                                                         purchaseInventories.getOrDefault(item.getItemId(), 0) + item.getNumberOfItems()));
        storeInventory.put(purchaseOrder.getStoreId(), purchaseInventories);
        purchaseOrder.getItems().forEach(item -> {
            Map<Integer, Integer> itemSale = itemInventory.getOrDefault(item.getItemId(),
                                                                        new HashMap<>());
            itemSale.put(purchaseOrder.getStoreId(),
                         itemSale.getOrDefault(purchaseOrder.getStoreId(), 0) + item.getNumberOfItems());
            itemInventory.put(item.getItemId(), itemSale);
        });
    }
}
