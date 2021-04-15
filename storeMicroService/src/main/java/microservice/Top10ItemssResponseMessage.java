package microservice;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Top10ItemssResponseMessage {
    private List<Top10ItemsSold> stores;
}
