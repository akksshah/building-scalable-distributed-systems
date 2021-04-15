package model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Purchase {
    private @NonNull Integer customerId;
    private @NonNull Integer storeId;
    private @NonNull Date date;
    private Integer id;
    private @NonNull Integer numberOfItems;
    private @NonNull String itemId;
}
