import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import lombok.var;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        var properties = new Properties();
        var propertiesFileName = "config.properties";
        var inputStream = ClientApp.class.getClassLoader().getResourceAsStream(propertiesFileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Property file: " + propertiesFileName + " does not exist");
        } else {
            properties.load(inputStream);
        }
        var config = ConfigParameter.getConfig(properties);
        System.err.println("===============================================================================");
        config.setMaxStores(256);
        new StoreFactory(config).execute();
        System.err.println("===============================================================================");
        config.setMaxStores(512);
        new StoreFactory(config).execute();
        System.err.println("===============================================================================");
    }
}
