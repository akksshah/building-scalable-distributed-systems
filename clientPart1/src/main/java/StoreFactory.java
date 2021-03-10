import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.var;

public class StoreFactory {
    private final ConfigParameter config;
    private final RequestCounter counter;

    public StoreFactory(ConfigParameter configParameter) {
        this.config = configParameter;
        this.counter = new RequestCounter();
    }

    public void execute() {
        System.out.println("Starting Execution at: " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss.SSS"))));
        var numberOfThreadsRequiredForPhaseOne = config.getMaxStores() / 4;
        var numberOfThreadsRequiredForPhaseTwo = config.getMaxStores() / 4;
        var numberOfThreadsRequiredForPhaseThree = config.getMaxStores() - numberOfThreadsRequiredForPhaseOne - numberOfThreadsRequiredForPhaseTwo;
        var worker_pool = Executors.newFixedThreadPool(config.getMaxStores());
        var startTime = System.currentTimeMillis();
        try {
            launchStores(worker_pool, numberOfThreadsRequiredForPhaseOne, 9, 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            launchStores(worker_pool, numberOfThreadsRequiredForPhaseTwo, 9, 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            launchStores(worker_pool, numberOfThreadsRequiredForPhaseThree, 9, 9);
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
        System.out.println("Total request sent: " + counter.getTotalRequestSent());
        System.out.println("Total request successful: " + counter.getNumberOfSuccessfulRequest());
        System.out.println("Total unsuccessful request: " + counter.getNumberOfFailedRequest());
        System.out.println("Total wall time: " + (endTime - startTime) + "ms");
        System.out.println("Throughput: " + (((double) counter.getTotalRequestSent()) / ((endTime - startTime) / 1000)));
        System.out.println("Ending execution at: " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss.SSS"))));
    }

    private void launchStores(ExecutorService workers, int numberOfThreadsToExecute, int hoursOpen, int countDownHours) throws InterruptedException {
        var latch = new CountDownLatch(numberOfThreadsToExecute * countDownHours);
        // System.out.println("CountDown for this phase: " + latch.getCount());
        IntStream.range(0, numberOfThreadsToExecute)
            .mapToObj(i -> new Store(config.getServerIpAddress(), config.getServerPort(), config.getNumPurchases(), 1
            , i + 1, config.getNumberOfCustomersPerStore(), config.getMaximumItemId(), hoursOpen, counter, latch))
            .forEach(workers::submit);
        latch.await();
    }
}
