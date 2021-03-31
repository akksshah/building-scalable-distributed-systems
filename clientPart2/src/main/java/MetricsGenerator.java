import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import lombok.var;

public final class MetricsGenerator {
    public static double getMeanResponseTime(List<RequestTracker> requests) {
        return requests.stream().mapToDouble(RequestTracker::getLatency).sum() / requests.size();
    }

    public static double getMedianResponseTime(List<RequestTracker> request) {
        var sortedLatency = request.stream().map(RequestTracker::getLatency).collect(Collectors.toList());
        return request.size() % 2 == 1
                   ? sortedLatency.get(sortedLatency.size() / 2)
                   : (sortedLatency.get((sortedLatency.size() - 1) / 2) + sortedLatency.get(sortedLatency.size() / 2)) / 2.0;
    }

    public static double getPercentileResponseTime(double percentile, List<RequestTracker> requests) {
        var sortedList = requests.stream()
                    .map(RequestTracker::getLatency)
                    .sorted((o1, o2) -> (int) (o2 - o1))
                    .limit((int) (Math.floor(100 - percentile) * requests.size() / 100))
                    .collect(Collectors.toList());
        return sortedList.get(sortedList.size() - 1);
    }

    public static OptionalDouble getMaxResponseTime(List<RequestTracker> requests) {
        return requests.stream().mapToDouble(RequestTracker::getLatency).max();
    }
}
