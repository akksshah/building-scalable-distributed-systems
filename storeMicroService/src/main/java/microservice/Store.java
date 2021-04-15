package microservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class Store {
    private static final Map<Integer, Map<String, Integer>> storeInventory = new ConcurrentHashMap<>();
    public static final Map<String, Map<Integer, Integer>> itemInventory = new ConcurrentHashMap<>();
    // storeId: {itemId: numberOfItemsSold} // top10ItemsInStore
    @Getter
    private static final Map<Integer, Map<String, Integer>> inventorySoldInStore =
            Collections.synchronizedMap(storeInventory);
    // itemId: {storeId: numberOfItemsSold}
    @Getter
    private static final Map<String, Map<Integer, Integer>> inventorySold =
            Collections.synchronizedMap(itemInventory);
    private static boolean started = false;
    private static final List<StoreConsumer> STORE_CONSUMERS = new ArrayList<>();
    private static final List<Thread> store_threads = new ArrayList<>();
    private static ExecutorService workers = Executors.newFixedThreadPool(256);

    public static void start() {
        System.out.println("MAIN");
        if (!started) {
            System.out.println("Starting up");
            for (int i = 0 ; i < 1532 ; i++) {
                StoreConsumer consumer = new StoreConsumer(Store.getInventorySoldInStore(),
                                                           Store.getInventorySold(),
                                                           ConnectionUtility.getConnection(), i,
                                                           0, workers, false);
                STORE_CONSUMERS.add(consumer);
                Thread thread  = new Thread(consumer);
                thread.start();
                store_threads.add(thread);
            }
            started = !started;
        }
    }

    public static void shutdown() {
        System.exit(0);
    }

    public static void main(String[] args) {
        start();
    }
}
