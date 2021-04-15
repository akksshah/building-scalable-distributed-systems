package util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {
    private static ObjectMapper mapper;

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }
}
