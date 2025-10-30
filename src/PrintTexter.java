import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class PrintTexter {

    private final Lock textLock = new ReentrantLock();// The main lock to ensure exclusive access to the printing logic.
    private final Condition textCondition;            // The condition (waiting room) threads use to 'await()' their turn.
    private volatile int currentOrder = 0;            // Tracks whose turn it is (0, 1, 2...). 'volatile' ensures visibility.
    private final int totalThreads;                   // The total number of threads that will be using this printer (e.g., 4).

    /**
     * @param totalThreads The total number of threads that will be
     * competing to print (e.g., 4).
     */
    public PrintTexter(int totalThreads) {
        this.totalThreads = totalThreads;
        this.textCondition = textLock.newCondition(); // Initialize the condition variable linked to our specific lock.
    }

    public void printText(String text, int order) {
        textLock.lock();

        try {

            while (currentOrder != order) {
                textCondition.await();
            }
            System.out.println("\n=====================================");
            System.out.print(">>> ");
            for (char c : text.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("\n=====================================");

            currentOrder = (currentOrder + 1) % totalThreads;

            textCondition.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
            // Восстанавливаем флаг прерывания
            Thread.currentThread().interrupt();
        } finally {
            textLock.unlock();
        }
    }
}