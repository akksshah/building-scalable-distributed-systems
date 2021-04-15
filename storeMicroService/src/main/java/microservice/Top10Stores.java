package microservice;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Top10Stores {
    private Integer storeId;
    private Integer totalItemsSold = 0;

    public Top10Stores(Integer storeId, int totalItemsSold) {
        this.storeId = storeId;
        this.totalItemsSold += totalItemsSold;
    }

    private synchronized void addTotalItemsSold(int num) {
        this.totalItemsSold += num;
    }
}
