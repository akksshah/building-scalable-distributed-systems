package util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PurchaseDeserializer extends StdDeserializer<PurchasedItems> {

    @SuppressWarnings("unused")
    protected PurchaseDeserializer() {
        super(PurchaseDeserializer.class);
    }

    @SuppressWarnings("unused")
    protected PurchaseDeserializer(Class<?> vc) {
        super(vc);
    }

    @SuppressWarnings("unused")
    protected PurchaseDeserializer(JavaType valueType) {
        super(valueType);
    }

    @SuppressWarnings("unused")
    protected PurchaseDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public PurchasedItems deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        int id = (Integer) node.get("numberOfItems:").numberValue();
        String itemName = node.get("ItemID").asText();
        return new PurchasedItems(itemName, id);
    }
}
