import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * The Consumer thread (now implements Runnable).
 * It takes a specific number of items ('Z' parameter) from the BlockingQueue
 * and then finishes.
 */
public class Picker implements Runnable {

    private final BlockingQueue<Integer> queue;
    private final int totalItemsToConsume; // 'Z' parameter
    private final Random random = new Random();
    private final int sleepTimeMs;
    private final String name; // Thread name
    private final CountDownLatch latch; // The latch to count down
    private int itemsConsumed = 0;

    public Picker(String name, BlockingQueue<Integer> queue, int totalItemsToConsume, int sleepTimeMs, CountDownLatch latch) {
        this.name = name;
        this.queue = queue;
        this.totalItemsToConsume = totalItemsToConsume;
        this.sleepTimeMs = sleepTimeMs;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            // This thread runs until it is "satisfied"
            while (itemsConsumed < totalItemsToConsume) {
                // 1. Get one item.
                // This method will BLOCK automatically if the queue is empty,
                // until an item becomes available.
                int item = queue.take();
                itemsConsumed++;

                System.out.println(name + " got: " + item
                        + " (" + itemsConsumed + "/" + totalItemsToConsume + ")"
                        + ". Stock is now: " + queue.size());

                // 2. Sleep to simulate consumption time
                Thread.sleep(random.nextInt(sleepTimeMs));
            }
        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted.");
            Thread.currentThread().interrupt(); // Restore the interrupted status
        } finally {
            // 3. When the loop finishes (or if an exception occurs),
            // the thread is done. We must count down the latch.
            System.out.println("===== " + name + " is SATISFIED ("
                    + itemsConsumed + " items) and FINISHED. =====");
            latch.countDown(); // Signal to the main thread that this consumer is done
        }
    }
}