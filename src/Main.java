import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main class for Laboratory Work 5.
 * This simulation uses ExecutorService and BlockingQueue.
 *
 * Logic:
 * 1. Create an ExecutorService (thread pool).
 * 2. Create an ArrayBlockingQueue (this replaces TheDepot).
 * 3. Create a CountDownLatch to wait for all Y Consumers.
 * 4. Submit X Producer (Stocker) tasks and Y Consumer (Picker) tasks.
 * 5. Wait for all Consumers to finish using latch.await().
 * 6. Forcefully shut down the executor (shutdownNow()), which will
 * interrupt the "infinite" Producer tasks.
 */
public class Main {

    public static void main(String[] args) {

        // --- Simulation Parameters (from your Lab 4 variant) ---
        int X = 2; // X: Number of Stockers (Producers)
        int Y = 11; // Y: Number of Pickers (Consumers)
        int Z = 3; // Z: Items each Picker needs (Goal)
        int D = 8; // D: Max size of Depot (Queue Capacity)
        int F = 2; // F: Items per production batch
        // --- End of Parameters ---

        // --- Helper Parameters ---
        int PRODUCER_SLEEP_MS = 200; // Max sleep time for producers
        int CONSUMER_SLEEP_MS = 300; // Max sleep time for consumers
        int MAX_GENERATED_NUMBER = 100; // Generator will make even numbers up to 100

        // 1. Create the shared resource: A Blocking Queue
        // This class automatically handles all synchronization (wait/notify/lock)
        BlockingQueue<Integer> depotQueue = new ArrayBlockingQueue<>(D);

        // 2. Create the Item Generator
        ItemGenerator generator = new EvenNumberGenerator(MAX_GENERATED_NUMBER);

        // 3. Create the Thread Pool
        // We create a pool with enough threads for all workers
        ExecutorService executor = Executors.newFixedThreadPool(X + Y);

        // 4. Create a CountDownLatch
        // This will block the main thread until Y tasks have counted down
        CountDownLatch latch = new CountDownLatch(Y);

        System.out.println("Starting simulation (Lab 5: ExecutorService) with " + X + " Producers and "
                + Y + " Consumers.");
        System.out.println("Parameters: X=" + X + ", Y=" + Y + ", Z=" + Z + ", D=" + D + ", F=" + F);

        // 5. Create and submit Producer (Stocker) tasks
        for (int i = 0; i < X; i++) {
            Stocker stocker = new Stocker(
                    "Stocker-" + (i + 1),
                    depotQueue,
                    generator,
                    F,
                    PRODUCER_SLEEP_MS
            );
            executor.submit(stocker); // Use submit() instead of thread.start()
        }

        // 6. Create and submit Consumer (Picker) tasks
        for (int i = 0; i < Y; i++) {
            Picker picker = new Picker(
                    "Picker-" + (i + 1),
                    depotQueue,
                    Z,
                    CONSUMER_SLEEP_MS,
                    latch       // Pass the latch
            );
            executor.submit(picker);
        }

        // 7. Wait for ALL Consumers to finish
        try {
            // The main thread will sleep here until the latch count reaches 0
            System.out.println("\n--- Main thread is waiting for all " + Y + " Consumers to finish... ---\n");
            latch.await();
        } catch (InterruptedException e) {
            System.err.println("Main thread was interrupted while waiting");
            Thread.currentThread().interrupt();
        }

        // 8. All Consumers are satisfied
        System.out.println("====================================================");
        System.out.println("ALL CONSUMERS ARE SATISFIED. SIMULATION ENDING.");
        System.out.println("====================================================");

        // 9. Forcefully stop the thread pool
        // This will send an InterruptedException to all running threads,
        // stopping the "infinite" loops in the Stocker tasks.
        executor.shutdownNow();

        try {
            // Wait a short time for tasks to clean up
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Thread pool did not terminate gracefully.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}