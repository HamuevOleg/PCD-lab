// Класс для Задач 3 и 4
public class Interval implements Runnable {
    int start;           // The starting number of the interval (e.g., 222 or 3333)
    int end;             // The ending number of the interval (e.g., 999 or 3999)
    boolean flag;        // Determines a direction: true for forward (Th3), false for backward (Th4)
    PairBarrier barrier; // The specific barrier (e.g., lock34) shared with its partner thread
    PrintTexter printer; // The common printer used by all tasks to print text in order
    String text;         // The text this task will print (e.g., "CR-233" or the discipline name)
    int order;           // This task's a specific slot (0-3) in the final printing queue


    /**
     * @param start   The starting number of the interval.
     * @param end     The ending number of the interval.
     * @param flag    The direction (true for forward, false for backward).
     * @param barrier The specific barrier this task shares with its partner.
     * @param printer The single, shared printer object for all threads.
     * @param text    The final text this task will print.
     * @param order   This task's position in the printing queue (0-3).
     */
    public Interval(int start, int end, boolean flag, PairBarrier barrier,
                        PrintTexter printer, String text, int order) {
        this.start = start;
        this.end = end;
        this.flag = flag;
        this.barrier = barrier;
        this.printer = printer;
        this.text = text;
        this.order = order;
    }

    public void run() {
        String threadName = "Thread " + (flag ? "3" : "4");

        System.out.println("\n[" + threadName + "]: Starting work");

        if (flag) {
            for (int i = start; i <= end; i++) {
                System.out.print(i + " ");
                if ((i - start + 1) % 20 == 0) {
                    System.out.println();
                }
            }
        } else {
            for (int i = end; i >= start; i--) {
                System.out.print(i + " ");
                if ((end - i + 1) % 20 == 0) {
                    System.out.println();
                }
            }
        }
        System.out.println("\n[" + threadName + "]: Finished interval traversal");
        barrier.signalAndWait();
        printer.printText(text, order);
    }
}