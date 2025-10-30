public class Main {
    static int size = 100;
    static int[] array = new int[size];

    public static void main(String[] args) {
        System.out.println("========== LAB WORK #3 (Runnable Edition) ==========");
        System.out.println("Thread Synchronization\n");

        System.out.println("Generating array of " + size + " elements:");
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * 100) + 1;
            System.out.print(array[i] + " ");
            if ((i + 1) % 20 == 0) System.out.println();
        }
        System.out.println("\n");


        PairBarrier barrier12 = new PairBarrier();
        PairBarrier barrier34 = new PairBarrier();

        PrintTexter textPrinter = new PrintTexter(4);


        // --- Daniil Driga Part - Tasks - Var 6  ---
        Runnable task1 = new Task(array, true, barrier12, textPrinter, "Hamuev | Driga", 2); // true -> fwd
        Runnable task2 = new Task(array, false, barrier12, textPrinter, "Oleg | Daniil", 0);  // false -> bwd

        // --- Oleg Hamuev Part - Intervals - Var 9  ---
        Runnable task3 = new Interval(222, 999, true, barrier34, textPrinter, "Concurrent and Distributed Programming", 3);
        Runnable task4 = new Interval(3333, 9999, false, barrier34, textPrinter, "CR-233", 1);

        Thread thread1 = new Thread(task1, "Thread-Driga  | Hamuev (Th1)");
        Thread thread2 = new Thread(task2, "Thread-Daniil | Oleg (Th2)");
        Thread thread3 = new Thread(task3, "Thread-Interval-Fwd (Th3)");
        Thread thread4 = new Thread(task4, "Thread-Interval-Bwd (Th4)");

        System.out.println("========== STARTING ALL THREADS ==========\n");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n========== ALL THREADS FINISHED ==========");
    }
}