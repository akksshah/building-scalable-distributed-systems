import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import util.PurchasedItems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SerializerTest {
    @Test
    public void testSerializationAndDeserialization() {
        PurchasedItems purchasedItems = new PurchasedItems("hello", 4);
        ObjectMapper mapper = new ObjectMapper();
        try {
            // {"ItemID": "hello", "numberOfItems:" 4}
            assertEquals("{\"ItemID\":\"hello\",\"numberOfItems:\":4}",
                         mapper.writeValueAsString(purchasedItems));
            assertEquals(purchasedItems, mapper.readValue(mapper.writeValueAsString(purchasedItems),
                                                          PurchasedItems.class));
        } catch (JsonProcessingException e) {
            fail();
        }
    }
}
