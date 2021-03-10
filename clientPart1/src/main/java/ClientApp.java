import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import lombok.var;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        // System.out.println("Starting Application at: " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss.SSS"))));
        var properties = new Properties();
        var propertiesFileName = "config.properties";
        var inputStream = ClientApp.class.getClassLoader().getResourceAsStream(propertiesFileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Property file: " + propertiesFileName + " does not exist");
        } else {
            properties.load(inputStream);
        }
        var config = ConfigParameter.getConfig(properties);
        new StoreFactory(config).execute();
    }
}
