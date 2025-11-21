import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * The Producer thread (now implements Runnable).
 * It generates batches of items (F items) and puts them into the BlockingQueue.
 * It runs in an infinite loop until interrupted.
 */
public class Stocker implements Runnable {

    private final BlockingQueue<Integer> queue;
    private final ItemGenerator generator;
    private final int itemsPerProduction; // 'F' parameter
    private final Random random = new Random();
    private final String name; // Thread name

    public Stocker(String name, BlockingQueue<Integer> queue, ItemGenerator generator, int itemsPerProduction) {
        this.name = name;
        this.queue = queue;
        this.generator = generator;
        this.itemsPerProduction = itemsPerProduction;
    }

    public void run() {
        try {
            while (true) {
                // 1. Generate 'F' items and put them in the queue one by one
                for (int i = 0; i < itemsPerProduction; i++) {
                    int item = generator.generate();

                    // 2. Put item in the queue.
                    // This method will BLOCK automatically if the queue is full,
                    // until space becomes available.
                    queue.put(item);
                    System.out.println(name + " added: " + item
                            + ". Stock is now: " + queue.size());
                }

                System.out.println("--- " + name + " finished a batch of " + itemsPerProduction + " items. ---");
                Thread.sleep(random.nextInt(100)import java.util.Random;
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
                });
            }
        } catch (InterruptedException e) {
            // This block executes when executor.shutdownNow() is called
            System.out.println(name + " was interrupted and is stopping.");
            // Restore the interrupted status
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}