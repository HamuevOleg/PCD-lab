import java.util.*;
import java.util.concurrent.locks.*;

public class Main {
    static final int DEPOT_SIZE = 7;     // D - размер склада
    static final int TOTAL_ITEMS = 42;   // Z - всего объектов
    static final int NUM_CONSUMERS = 4;  // Y
    static final int NUM_PRODUCERS = 2;  // X (для твоего кента)

    /**
     * Хранилище (Depot) - синхронизированный буфер
     */
    static class Depot {
        private final Queue<Integer> items = new LinkedList<>();
        private final int capacity;
        private int totalProduced = 0;
        private int totalConsumed = 0;
        private boolean producersFinished = false;

        private final ReentrantLock lock = new ReentrantLock();
        private final Condition isFull = lock.newCondition();    // склад полон (для потребителей)
        private final Condition isEmpty = lock.newCondition();   // склад пуст (для производителей)

        Depot(int capacity) {
            this.capacity = capacity;
        }

        /**
         * Производитель добавляет объект
         */
        public void produce(int item) throws InterruptedException {
            lock.lock();
            try {
                // Ждём, пока склад освободится (не полный)
                while (items.size() >= capacity && totalConsumed < TOTAL_ITEMS) {
                    System.out.println("[СКЛАД ПОЛОН] Производитель ждёт. В складе: " + items.size() + "/" + capacity);
                    isEmpty.await();
                }

                if (totalConsumed >= TOTAL_ITEMS) {
                    return; // Готово, выходим
                }

                items.offer(item);
                totalProduced++;
                System.out.println("✓ Произведено: " + item + " | Склад: " + items.size() + "/" + capacity +
                        " | Всего произведено: " + totalProduced);

                // Сигнализируем потребителям, что появились данные
                isFull.signalAll();

            } finally {
                lock.unlock();
            }
        }

        /**
         * Потребитель забирает объект
         */
        public Integer consume(String consumerName) throws InterruptedException {
            lock.lock();
            try {
                // Ждём, пока склад заполнится или не закончится производство
                while (items.isEmpty() && !producersFinished) {
                    System.out.println("[СКЛАД ПУСТ] " + consumerName + " ждёт. Склад: " + items.size() + "/" + capacity);
                    isFull.await();
                }

                // Если склад пуст и производители закончили - ничего не возвращаем
                if (items.isEmpty()) {
                    return null;
                }

                Integer item = items.poll();
                totalConsumed++;
                System.out.println("✗ " + consumerName + " потребил: " + item + " | Склад: " + items.size() + "/" + capacity +
                        " | Всего потреблено: " + totalConsumed);

                // Сигнализируем производителям, что место освободилось
                isEmpty.signalAll();

                return item;

            } finally {
                lock.unlock();
            }
        }

        /**
         * Производитель сообщает, что закончил
         */
        public void producersFinished() {
            lock.lock();
            try {
                this.producersFinished = true;
                isFull.signalAll(); // Разбудим потребителей
            } finally {
                lock.unlock();
            }
        }

        public int getTotalConsumed() {
            lock.lock();
            try {
                return totalConsumed;
            } finally {
                lock.unlock();
            }
        }

        public int getTotalProduced() {
            lock.lock();
            try {
                return totalProduced;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * ПОТРЕБИТЕЛЬ
     */
    static class Consumer extends Thread {
        private final Depot depot;

        Consumer(Depot depot, String name) {
            super(name);
            this.depot = depot;
        }

        @Override
        public void run() {
            try {
                while (depot.getTotalConsumed() < TOTAL_ITEMS) {
                    Integer item = depot.consume(getName());

                    if (item == null) {
                        // Производители закончили, нечего больше брать
                        break;
                    }

                    // Имитация работы с объектом
                    Thread.sleep(50);
                }

                System.out.println("[ЗАВЕРШЕНО] " + getName() + " закончил работу");

            } catch (InterruptedException e) {
                System.out.println("[ОШИБКА] " + getName() + " прерван: " + e.getMessage());
            }
        }
    }


    static class Producer extends Thread {
        private final Depot depot;
        private static int nextOddNumber = 1; // Нечетные числа: 1, 3, 5, 7...

        Producer(Depot depot, String name) {
            super(name);
            this.depot = depot;
        }

        @Override
        public void run() {
            try {
                while (depot.getTotalProduced() < TOTAL_ITEMS) {
                    // Генерируем нечетное число
                    int item;
                    synchronized (Producer.class) {
                        item = nextOddNumber;
                        nextOddNumber += 2;
                    }

                    depot.produce(item);

                    // Имитация работы производства
                    Thread.sleep(100);
                }

                System.out.println("[ЗАВЕРШЕНО] " + getName() + " закончил производство");

            } catch (InterruptedException e) {
                System.out.println("[ОШИБКА] " + getName() + " прерван: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PRODUCER-CONSUMER ===");
        System.out.println("Производителей: " + NUM_PRODUCERS);
        System.out.println("Потребителей: " + NUM_CONSUMERS);
        System.out.println("Размер склада: " + DEPOT_SIZE);
        System.out.println("Всего объектов: " + TOTAL_ITEMS);
        System.out.println("================================\n");

        Depot depot = new Depot(DEPOT_SIZE);

        // Создаём потребителей
        Thread[] consumers = new Thread[NUM_CONSUMERS];
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new Consumer(depot, "Потребитель-" + (i + 1));
        }

        // Создаём производителей (для твоего кента)
        Thread[] producers = new Thread[NUM_PRODUCERS];
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            producers[i] = new Producer(depot, "Производитель-" + (i + 1));
        }

        // Запускаем всех
        for (Thread p : producers) p.start();
        for (Thread c : consumers) c.start();

        // Ждём завершения производителей
        for (Thread p : producers) p.join();
        depot.producersFinished(); // Сообщаем потребителям, что производство закончилось

        // Ждём завершения потребителей
        for (Thread c : consumers) c.join();

        System.out.println("\n================================");
        System.out.println("=== РАБОТА ЗАВЕРШЕНА ===");
        System.out.println("Всего произведено: " + depot.getTotalProduced());
        System.out.println("Всего потреблено: " + depot.getTotalConsumed());
        System.out.println("================================");
    }
}