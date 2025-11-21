import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        int X = 5;
        int Y = 8;
        int Z = 15;
        int D = 8;
        int F = 2;
        int MAX_NUM = 100;

        BlockingQueue<Integer> depotQueue = new ArrayBlockingQueue<>(D);
        ItemGenerator generator = new EvenNumberGenerator(MAX_NUM);
        ExecutorService executor = Executors.newFixedThreadPool(X + Y);

        AtomicInteger activePickers = new AtomicInteger(Y);

        Object monitor = new Object();

        System.out.println("=== SIMULATION STARTED ===");

        for (int i = 0; i < X; i++) {
            executor.submit(new Stocker("Stocker-" + (i + 1), depotQueue, generator, F, activePickers));
        }
        for (int i = 0; i < Y; i++) {
            executor.submit(new Picker("Picker-" + (i + 1), depotQueue, Z, activePickers, monitor));
        }

        synchronized (monitor) {
            while (activePickers.get() > 0) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\n--- All Pickers are gone. Stopping blocked Stockers... ---");

        executor.shutdownNow();

    }
}