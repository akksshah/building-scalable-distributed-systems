import java.util.Date;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
public class ConfigParameter {
    @NonNull
    private Integer maxStores;

    private Integer numberOfCustomersPerStore = 1000;

    private Integer maximumItemId = 100000;

    private int numPurchases = 60;

    @Setter(AccessLevel.NONE)
    private int numberOfItemsToPurchaseForEachPurchase = 5;

    @Setter(AccessLevel.NONE)
    private Date date = DateBuilder.getDate("20210101");

    @NonNull
    private String serverIpAddress;

    @NonNull
    private String serverPort;

    public void setNumberOfItemsToPurchaseForEachPurchase(int numberOfItemsToPurchaseForEachPurchase) {
        if (numberOfItemsToPurchaseForEachPurchase <= 20 && numberOfItemsToPurchaseForEachPurchase >= 1) {
            this.numberOfItemsToPurchaseForEachPurchase = numberOfItemsToPurchaseForEachPurchase;
        } else {
            throw new IllegalArgumentException("Range allowed is 1 - 20, Provided: " + numberOfItemsToPurchaseForEachPurchase);
        }
    }

    public void setDate(String s) {
        Date date = DateBuilder.getDate(s);
        if (date == null) {
            throw new IllegalArgumentException("Date format incorrect");
        }
        this.date = date;
    }

    private ConfigParameter() {

    }

    public static ConfigParameter getConfig(Properties properties) {
        ConfigParameter config = new ConfigParameter();
        config.setMaxStores(Integer.parseInt(properties.getProperty("maxStores")));
        config.setNumberOfCustomersPerStore(Integer.parseInt(properties.getProperty("numberOfCustomersPerStore")));
        config.setMaximumItemId(Integer.parseInt(properties.getProperty("maximumItemId")));
        config.setNumPurchases(Integer.parseInt(properties.getProperty("numPurchases")));
        config.setNumberOfItemsToPurchaseForEachPurchase(Integer.parseInt(properties.getProperty("numberOfItemsToPurchaseForEachPurchase")));
        config.setDate(properties.getProperty("date"));
        config.setServerIpAddress(properties.getProperty("serverIpAddress"));
        config.setServerPort(properties.getProperty("serverPort"));
        return config;
    }
}


