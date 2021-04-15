package microservice;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Top10ItemsSold {
    private String itemId;
    private Integer totalItemsSold = 0;

    public Top10ItemsSold(String itemId, int totalItemsSold) {
        this.itemId = itemId;
        this.totalItemsSold += totalItemsSold;
    }

    private synchronized void addTotalItemsSold(int num) {
        this.totalItemsSold += num;
    }
}
