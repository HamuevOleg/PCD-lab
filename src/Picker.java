import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Picker implements Runnable {

    private final BlockingQueue<Integer> queue;
    private final int totalItemsToConsume;
    private final String name;
    private final AtomicInteger activePickers;
    private final Object monitor;
    private int itemsConsumed = 0;

    public Picker(String name, BlockingQueue<Integer> queue, int totalItemsToConsume, AtomicInteger activePickers, Object monitor) {
        this.name = name;
        this.queue = queue;
        this.totalItemsToConsume = totalItemsToConsume;
        this.activePickers = activePickers;
        this.monitor = monitor;
    }

    public void run() {
        try {
            while (itemsConsumed < totalItemsToConsume) {
                if (queue.isEmpty()) {
                    System.out.println(name + ": Depot is EMPTY! Waiting...");
                }

                Integer item = queue.take();

                itemsConsumed++;
                System.out.println(name + " took: " + item +
                        " (" + itemsConsumed + "/" + totalItemsToConsume + ")");
            }
            System.out.println(name + " IS SATISFIED.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            int left = activePickers.decrementAndGet();
            System.out.println(name + " leaving. Pickers left: " + left);

            if (left == 0) {
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        }
    }
}