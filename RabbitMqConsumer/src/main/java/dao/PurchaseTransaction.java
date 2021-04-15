package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import lombok.var;
import model.Purchase;

public class PurchaseTransaction {
    private static final String INSERT_SQL_QUERY =
        "INSERT INTO purchase(store_id, customer_id, item_id, number_of_items, date) VALUES(?, ?, ?, ?, ?)";
    public void savePurchaseOrder(List<Purchase> order) {
        try (Connection connection = ConnectionUtility.getConnection()) {
            try {
                var preparedStatement = connection.prepareStatement(INSERT_SQL_QUERY);

                for (Purchase purchase : order) {
                    preparedStatement.setInt(1, purchase.getStoreId());
                    preparedStatement.setInt(2, purchase.getCustomerId());
                    preparedStatement.setString(3, purchase.getItemId());
                    preparedStatement.setInt(4, purchase.getNumberOfItems());
                    preparedStatement.setDate(5, purchase.getDate());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                System.err.println("Rolling back with reason: " + e.getMessage());
                connection.rollback();
                throw new RuntimeException("Roll Backed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to Roll back");
            throw new RuntimeException("Failed to insert/rollback into the database");
        }
    }
}
