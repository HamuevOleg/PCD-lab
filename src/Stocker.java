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
    private final int sleepTimeMs;
    private final String name; // Thread name

    public Stocker(String name, BlockingQueue<Integer> queue, ItemGenerator generator, int itemsPerProduction, int sleepTimeMs) {
        this.name = name;
        this.queue = queue;
        this.generator = generator;
        this.itemsPerProduction = itemsPerProduction;
        this.sleepTimeMs = sleepTimeMs;
    }

    @Override
    public void run() {
        try {
            // This thread runs indefinitely until interrupted
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

                // 3. Sleep to simulate production time
                Thread.sleep(random.nextInt(sleepTimeMs));
            }
        } catch (InterruptedException e) {
            // This block executes when executor.shutdownNow() is called
            System.out.println(name + " was interrupted and is stopping.");
            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }
    }
}