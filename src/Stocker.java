import java.util.ArrayList;
import java.util.Random;

/**
 * The Producer thread.
 * It generates batches of items (F items) using an ItemGenerator
 * and puts them into TheDepot.
 */
public class Stocker extends Thread {

    private final TheDepot depot;
    private final ItemGenerator generator;
    private final int itemsPerProduction; // 'F' parameter
    private final Random random = new Random();
    private final int sleepTimeMs;

    public Stocker(String name, TheDepot depot, ItemGenerator generator, int itemsPerProduction, int sleepTimeMs) {
        super(name); // Set thread name
        this.depot = depot;
        this.generator = generator;
        this.itemsPerProduction = itemsPerProduction;
        this.sleepTimeMs = sleepTimeMs;
    }

    @Override
    public void run() {
        try {
            // This thread runs indefinitely (as a daemon)
            while (true) {
                // 1. Generate a batch of 'F' items
                ArrayList<Integer> newItems = new ArrayList<>(itemsPerProduction);
                for (int i = 0; i < itemsPerProduction; i++) {
                    newItems.add(generator.generate());
                }

                // 2. Put them in the depot (this will block if full)
                depot.put(newItems);

                // 3. Sleep to simulate production time
                Thread.sleep(random.nextInt(sleepTimeMs));
            }
        } catch (InterruptedException e) {
            // Thread interrupted, will exit
            System.out.println(getName() + " was interrupted.");
        }
    }
}