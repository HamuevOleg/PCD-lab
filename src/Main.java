import java.util.ArrayList;
import java.util.List;

/**
 * The Main class to run the Producer-Consumer simulation.
 * All parameters (X, Y, Z, D, F) are defined and injected from here.
 */
public class Main {

    public static void main(String[] args) {

        // --- Simulation Parameters (from your variant) ---
        int X= 2; // X: Number of Stockers
        int Y = 11; // Y: Number of Pickers
        int Z = 3; // Z: Items each Picker needs
        int D = 8; // D: Max size of Depot
        int F = 2; // F: Items per production batch
        // --- End of Parameters ---

        // --- Helper Parameters ---
        int PRODUCER_SLEEP_MS = 200; // Max sleep time for producers
        int CONSUMER_SLEEP_MS = 300; // Max sleep time for consumers
        int MAX_GENERATED_NUMBER = 100; // Generator will make even numbers up to 100

        // 1. Create the single shared Depot
        TheDepot depot = new TheDepot(D);

        // 2. Create the Item Generator (dynamically)
        ItemGenerator generator = new EvenNumberGenerator(MAX_GENERATED_NUMBER);

        // 3. Create a list to hold Consumer threads (so we can wait for them)
        List<Picker> pickers = new ArrayList<>();

        System.out.println("Starting simulation with " + X + " Producers and "
                + Y + " Consumers.");

        // 4. Create and start Producers (Stockers)
        for (int i = 0; i < X; i++) {
            Stocker stocker = new Stocker(
                    "Stocker-" + (i + 1),
                    depot,
                    generator,
                    F,
                    PRODUCER_SLEEP_MS
            );
            // Set as Daemon so they don't prevent the program from exiting
            stocker.setDaemon(true);
            stocker.start();
        }

        // 5. Create and start Consumers (Pickers)
        for (int i = 0; i < Y; i++) {
            Picker picker = new Picker(
                    "Picker-" + (i + 1),
                    depot,
                    Z,
                    CONSUMER_SLEEP_MS
            );
            pickers.add(picker); // Add to list so we can 'join' them
            picker.start();
        }

        // 6. Wait for ALL Consumers to finish
        // The main thread will block here until every 'picker.join()' returns.
        for (Picker picker : pickers) {
            try {
                picker.join(); // Wait for this thread to die
            } catch (InterruptedException e) {
                System.err.println("Main thread join interrupted");
            }
        }

        // 7. All Pickers are finished
        System.out.println("====================================================");
        System.out.println("ALL CONSUMERS ARE SATISFIED. SIMULATION ENDING.");
        System.out.println("====================================================");
    }
}