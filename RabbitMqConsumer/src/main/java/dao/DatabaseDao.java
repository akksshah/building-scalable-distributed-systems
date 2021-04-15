package dao;

import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import model.Purchase;
import util.Order;

@AllArgsConstructor
public class DatabaseDao implements Runnable {
    private final Order purchaseOrder;
    private final PurchaseTransaction DAO = new PurchaseTransaction();

    @Override
    public void run() {
        try {
            DAO.savePurchaseOrder(
                    purchaseOrder
                            .getItems()
                            .stream()
                            .map(item ->
                                         new Purchase(
                                                 purchaseOrder.getCustomerId(),
                                                 purchaseOrder.getStoreId(),
                                                 purchaseOrder.getDate(),
                                                 item.getNumberOfItems(),
                                                 item.getItemId())).collect(Collectors
                                                                                    .toList()));
        } catch (Exception e) {
            System.err.println("Save failed");
        }
    }
}
