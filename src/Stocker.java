import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Stocker implements Runnable {

    private final BlockingQueue<Integer> queue;
    private final ItemGenerator generator;
    private final String name;
    private final int itemsPerBatch;
    private final AtomicInteger activePickers;

    public Stocker(String name, BlockingQueue<Integer> queue, ItemGenerator generator, int itemsPerBatch, AtomicInteger activePickers) {
        this.name = name;
        this.queue = queue;
        this.generator = generator;
        this.itemsPerBatch = itemsPerBatch;
        this.activePickers = activePickers;
    }

    public void run() {
        try {
            while (activePickers.get() > 0) {
                for (int i = 0; i < itemsPerBatch; i++) {
                    if (activePickers.get() == 0) break;

                    int item = generator.generate();

                    if (queue.remainingCapacity() == 0) {
                        System.out.println(name + ": Depot is [FULL]! Waiting...");
                    }
                    queue.put(item);

                    System.out.println(name + " added: " + item + ". Stock: " + queue.size());
                }
            }
            System.out.println(name + " sees Pickers are gone. Filling the rest...");

            while (queue.remainingCapacity() > 0) {
                int item = generator.generate();
                if (queue.offer(item)) {
                    System.out.println(name + " (FINAL FILL) added: " + item + ". Stock: " + queue.size());
                } else {
                    break;
                }
            }

            System.out.println(name + " FINISHED WORK.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}