import java.util.Random;

/**
 * The Consumer thread.
 * It takes a specific number of items ('Z' parameter) from TheDepot
 * and then finishes.
 */
public class Picker extends Thread {

    private final TheDepot depot;
    private final int totalItemsToConsume; // 'Z' parameter
    private final Random random = new Random();
    private final int sleepTimeMs;
    private int itemsConsumed = 0;

    public Picker(String name, TheDepot depot, int totalItemsToConsume, int sleepTimeMs) {
        super(name); // Set thread name
        this.depot = depot;
        this.totalItemsToConsume = totalItemsToConsume;
        this.sleepTimeMs = sleepTimeMs;
    }

    @Override
    public void run() {
        try {
            // This thread runs until it is "satisfied"
            while (itemsConsumed < totalItemsToConsume) {
                // 1. Get one item (this will block if empty)
                int item = depot.get();
                itemsConsumed++;

                // 2. Sleep to simulate consumption time
                Thread.sleep(random.nextInt(sleepTimeMs));
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted.");
        }

        // 3. When the loop finishes, the thread is done
        System.out.println("===== " + getName() + " is SATISFIED ("
                + itemsConsumed + " items) and FINISHED. =====");
    }
}