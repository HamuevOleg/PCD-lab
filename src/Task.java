// For Th1 and Th2
public class Task implements Runnable {
    private int[] array;         // the shared array of numbers that this task will process.
    private boolean flag;        // true - th goes forward, false - th goes backward
    private PairBarrier barrier; // common barrier for pair lock12
    private PrintTexter printer; // common printer for all threads
    private String text;         // the specific text that this task will queue up to be printed after its main work is done.
    private int order;           // the order of this task in the queue.

    /**
     * @param array   The shared array to process.
     * @param flag    The direction flag (true for forward, false for backward).
     * @param barrier The specific PairBarrier this task shares with its partner.
     * @param printer The single, shared printer object for all threads.
     * @param text    The final text this task will print.
     * @param order   This task's position in the printing queue (0-3).
     */

    Task(int[] array, boolean flag, PairBarrier barrier, PrintTexter printer, String text, int order) {
        this.array = array;
        this.flag = flag;
        this.barrier = barrier;
        this.printer = printer;
        this.text = text;
        this.order = order;
    }

    public void run() {
        int totalSum = 0;
        String threadName = Thread.currentThread().getName();

        if (flag) {
            System.out.println("[" + threadName + "] (Array Fwd): Starting work");
            for (int i = 0; i < array.length - 1; i += 4) {
                if (i + 2 < array.length) {
                    int product = array[i] * array[i + 2];
                    totalSum += product;
                    System.out.println("[" + threadName + "]: array[" + i + "] * array[" + (i+2) + "] = " + product);
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                }
            }
            System.out.println("[" + threadName + "] (Array Fwd): Total sum = " + totalSum);

        } else {

            System.out.println("[" + threadName + "] (Array Bwd): Starting work");

            int startPos = array.length - 1;
            if (startPos % 2 == 1) {
                startPos--;
            }
            for (int i = startPos; i >= 2; i -= 4) {
                int product = array[i] * array[i - 2];
                totalSum += product;
                System.out.println("[" + threadName + "]: array[" + i + "] * array[" + (i-2) + "] = " + product);
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
            System.out.println("[" + threadName + "] (Array Bwd): Total sum = " + totalSum);
        }

        barrier.signalAndWait();

        printer.printText(text, order);
    }
}