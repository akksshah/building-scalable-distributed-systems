package shared;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NonNull;

@Data
public class Item {
    // TODO: more robust validation for values to be non null
    @SerializedName("ItemId")
    private @NonNull String itemId;

    @SerializedName("numberOfItems:")
    private @NonNull String numberOfItems;
}
