package microservice;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Top10ItemsResponseMessage {
    List<Top10Stores> stores;
}
