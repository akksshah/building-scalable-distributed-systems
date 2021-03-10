package shared;

import java.util.List;

import lombok.Data;

@Data
public class PurchaseOrder {
    private List<Item> items;
}
