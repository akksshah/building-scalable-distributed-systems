package util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
//@JsonDeserialize(using = PurchaseDeserializer.class)
@JsonSerialize(using = PurchaseSerializer.class)
@EqualsAndHashCode
public class PurchasedItems {
    private String itemId;
    private Integer numberOfItems;
}
