import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class RequestCounter {
    private int numberOfSuccessfulRequest;

    private int numberOfFailedRequest;

    public synchronized void requestFailed() {
        numberOfFailedRequest++;
    }

    public synchronized void requestSucceeded() {
        numberOfSuccessfulRequest++;
    }

    public int getTotalRequestSent() {
        return numberOfSuccessfulRequest + numberOfFailedRequest;
    }
}
