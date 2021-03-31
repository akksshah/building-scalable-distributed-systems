import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.var;

public class StoreFactory {
    private final ConfigParameter config;
    private final RequestCounter counter;
    private final List<RequestTracker> requestsTracker;
    private final int hoursOpen;

    public StoreFactory(ConfigParameter configParameter) {
        config = configParameter;
        counter = new RequestCounter();
        requestsTracker = Collections.synchronizedList(new ArrayList<>());
        hoursOpen = 9;
    }

    public void execute() {
        System.out.println("Starting Execution at: " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss.SSS"))));
        var numberOfThreadsRequiredForPhaseOne = config.getMaxStores() / 4;
        var numberOfThreadsRequiredForPhaseTwo = config.getMaxStores() / 4;
        var numberOfThreadsRequiredForPhaseThree = config.getMaxStores() - numberOfThreadsRequiredForPhaseOne - numberOfThreadsRequiredForPhaseTwo;
        var worker_pool = Executors.newFixedThreadPool(config.getMaxStores());
        var startTime = System.currentTimeMillis();
        try {
            System.out.println("Launching stores for eastern time");
            launchStores(worker_pool, numberOfThreadsRequiredForPhaseOne, hoursOpen, 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Launching stores for central time");
            launchStores(worker_pool, numberOfThreadsRequiredForPhaseTwo, hoursOpen, 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Launching stores for pacific time");
            launchStores(worker_pool, numberOfThreadsRequiredForPhaseThree, hoursOpen, 9);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        worker_pool.shutdown();
        try {
            if (!worker_pool.awaitTermination(1, TimeUnit.HOURS)) {
                worker_pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            worker_pool.shutdownNow();
        }
        var endTime = System.currentTimeMillis();
        System.out.println("********** Statistics **********");
        System.out.println("Total number of stores used for simulation: ");
        System.out.println("Total request sent: " + counter.getTotalRequestSent());
        System.out.println("Total request successful: " + counter.getNumberOfSuccessfulRequest());
        System.out.println("Total unsuccessful request: " + counter.getNumberOfFailedRequest());
        System.out.println("Total wall time: " + ((endTime - startTime) / 1000) + "s");
        System.out.println("Throughput: " + (((double) counter.getTotalRequestSent()) / ((endTime - startTime) / 1000)));
        System.out.println("Mean response time: " + MetricsGenerator.getMeanResponseTime(requestsTracker) + "ms");
        System.out.println("Median response time: " + MetricsGenerator.getMedianResponseTime(requestsTracker) + "ms");
        System.out.println("p99 (99 percentile): " + MetricsGenerator.getPercentileResponseTime(99, requestsTracker) + "ms");
        System.out.println("Max response time: " + MetricsGenerator.getMaxResponseTime(requestsTracker) + "ms");
        try {
            CsvWriter.writeToCsv(config.getMaxStores() + "Threads.csv", requestsTracker);
        } catch (FileNotFoundException e) {
            System.err.println("Could not save the request data to Csv");
        }
        System.out.println("Ending execution at: " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss.SSS"))));
    }

    private void launchStores(ExecutorService workers, int numberOfThreadsToExecute, int hoursOpen, int countDownHours) throws InterruptedException {
        var latch = new CountDownLatch(numberOfThreadsToExecute * countDownHours);
        // System.out.println("CountDown for this phase: " + latch.getCount());
        IntStream.range(0, numberOfThreadsToExecute)
            .mapToObj(i -> new Store(config.getServerIpAddress(), config.getNumPurchases(), 1
            , i + 1, config.getNumberOfCustomersPerStore(), config.getMaximumItemId(), hoursOpen, counter, latch, requestsTracker))
            .forEach(workers::submit);
        latch.await();
    }
}
