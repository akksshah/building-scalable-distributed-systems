package util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PurchaseSerializer extends StdSerializer<PurchasedItems> {

    @SuppressWarnings("unused")
    public PurchaseSerializer() {
        this(null);
    }

    @SuppressWarnings("unused")
    public PurchaseSerializer(Class<PurchasedItems> vc) {
        super(vc);
    }

    @Override
    public void serialize(PurchasedItems purchasedItems, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("ItemID",purchasedItems.getItemId());
        jsonGenerator.writeNumberField("numberOfItems:", purchasedItems.getNumberOfItems());
        jsonGenerator.writeEndObject();
    }
}
