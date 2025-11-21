import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Main class for Laboratory Work 5.
 * Clean version: No CountDownLatch, No sleeps, simplified logic.
 */
public class Main {

    public static void main(String[] args) {
        // --- Simulation Parameters ---
        int X = 5; // Stockers (Producers)
        int Y = 2; // Pickers (Consumers)
        int Z = 3; // Items per Picker (Goal)
        int D = 4; // Depot Capacity
        int F = 2; // Items per batch
        int MAX_GENERATED_NUMBER = 100;

        // 1. Shared Resource (Queue)
        BlockingQueue<Integer> depotQueue = new ArrayBlockingQueue<>(D);

        // 2. Item Generator
        ItemGenerator generator = new EvenNumberGenerator(MAX_GENERATED_NUMBER);

        // 3. Thread Pool
        ExecutorService executor = Executors.newFixedThreadPool(X + Y);

        System.out.println("Starting simulation (Clean Version)...");
        System.out.println("Params: X=" + X + ", Y=" + Y + ", Z=" + Z + ", D=" + D + ", F=" + F);

        // 4. Submit Stockers (Producers)
        // We don't need to keep their references, they run indefinitely.
        for (int i = 0; i < X; i++) {
            Stocker stocker = new Stocker(
                    "Stocker-" + (i + 1),
                    depotQueue,
                    generator,
                    F
            );
            executor.submit(stocker);
        }

        // 5. Submit Pickers (Consumers) and KEEP their Futures
        // We need these Futures to know when consumers are done.
        List<Future<?>> pickerFutures = new ArrayList<>();
        for (int i = 0; i < Y; i++) {
            Picker picker = new Picker(
                    "Picker-" + (i + 1),
                    depotQueue,
                    Z
            );
            // executor.submit returns a Future representing the task
            pickerFutures.add(executor.submit(picker));
        }

        // 6. Wait for all Pickers to finish
        System.out.println("\n--- Main thread is waiting for Consumers via Futures... ---\n");

        for (Future<?> future : pickerFutures) {
            try {
                // .get() blocks the main thread until this specific task is complete
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 7. Finish
        System.out.println("====================================================");
        System.out.println("ALL CONSUMERS FINISHED. STOPPING SYSTEM.");
        System.out.println("====================================================");

        // 8. Stop the Stockers (Interrupt them)
        executor.shutdownNow();
    }
}