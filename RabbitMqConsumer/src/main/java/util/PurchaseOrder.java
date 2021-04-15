package util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.PurchaseItems;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurchaseOrder {
    List<PurchaseItems> items;
}
