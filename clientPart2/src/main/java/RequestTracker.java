import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestTracker {
    private Long startTime;

    private Long endTime;

    private RequestType requestType;

    private Integer responseCode;

    public long getLatency() {
        return endTime - startTime;
    }
}
