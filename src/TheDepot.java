import java.util.ArrayList;
import java.util.List;

/**
 * The shared resource (Store/Buffer).
 * This class is thread-safe and manages concurrent access from Producers and Consumers.
 */
public class TheDepot {

    private final ArrayList<Integer> stock;
    private final int maxSize; // 'D' parameter

    /**
     * Creates a depot with a maximum capacity.
     * @param maxSize The 'D' parameter (maximum number of items).
     */
    public TheDepot(int maxSize) {
        this.maxSize = maxSize;
        this.stock = new ArrayList<>(maxSize);
    }

    /**
     * Called by Producers (Stockers) to add items.
     * @param items A list of items to add (F items).
     */
    public synchronized void put(List<Integer> items) {

        // Проверяем, есть ли место для ВСЕЙ партии
        while (stock.size() + items.size() > maxSize) {
            try {
                // Если места нет, поток-производитель "засыпает"
                // и ОСВОБОЖДАЕТ МОНИТОР (замок),
                // позволяя другим потокам (потребителям) работать.
                System.out.println("--- DEPOT FULL (" + stock.size() + "/" + maxSize + "). "
                        + Thread.currentThread().getName() + " is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                System.err.println("Producer wait interrupted");
            }
        }

        // Когда поток проснулся и место есть:
        stock.addAll(items);
        System.out.println(Thread.currentThread().getName() + " added " + items
                + ". Stock is now: " + stock.size());

        // "Будим" ВСЕ потоки (особенно потребителей),
        // которые могли уснуть на пустом складе.
        notifyAll();
    }

    /**
     * Called by Consumers (Pickers) to get one item.
     * @return The item taken from the depot.
     */
    public synchronized int get() {

        // Проверяем, пуст ли склад
        while (stock.isEmpty()) {
            try {
                // Если склад пуст, поток-потребитель "засыпает"
                // и ОСВОБОЖДАЕТ МОНИТОР (замок),
                // позволяя другим потокам (производителям) работать.
                System.out.println("--- DEPOT EMPTY. "
                        + Thread.currentThread().getName() + " is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                System.err.println("Consumer wait interrupted");
            }
        }

        // Когда поток проснулся и товар есть:
        // (Удаляем с конца, это O(1) для ArrayList)
        int item = stock.remove(stock.size() - 1);
        System.out.println(Thread.currentThread().getName() + " got: " + item
                + ". Stock is now: " + stock.size());

        // "Будим" ВСЕ потоки (особенно производителей),
        // которые могли уснуть на полном складе.
        notifyAll();

        return item;
    }
}