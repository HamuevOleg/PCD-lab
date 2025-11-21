import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * The Consumer thread (implements Runnable).
 * It takes a specific number of items ('Z' parameter) from the BlockingQueue
 * and then finishes.
 */
public class Picker implements Runnable {

    private final BlockingQueue<Integer> queue;
    private final int totalItemsToConsume; // 'Z' parameter
    private final Random random = new Random();
    private final String name; // Thread name
    private final CountDownLatch latch; // The latch to count down
    private int itemsConsumed = 0;

    public Picker(String name, BlockingQueue<Integer> queue, int totalItemsToConsume, CountDownLatch latch) {
        this.name = name;
        this.queue = queue;
        this.totalItemsToConsume = totalItemsToConsume;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            while (itemsConsumed < totalItemsToConsume) {
                // 1. Get one item. (BLOCKS if empty)
                int item = queue.take();
                itemsConsumed++;

                System.out.println(name + " got: " + item
                        + " (" + itemsConsumed + "/" + totalItemsToConsume + ")"
                        + ". Stock is now: " + queue.size());

                // 2. Sleep to simulate consumption time
                Thread.sleep(random.nextInt(100));
            }
        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            // 3. Signal that this consumer is done
            System.out.println("===== " + name + " is SATISFIED ("
                    + itemsConsumed + " items) and FINISHED. =====");
            latch.countDown();
        }
    }
}