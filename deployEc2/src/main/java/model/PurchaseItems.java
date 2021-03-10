package model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import util.PurchaseDeserializer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonDeserialize(using = PurchaseDeserializer.class)
public class PurchaseItems {
    private String itemId;
    private Integer numberOfItems;
}
