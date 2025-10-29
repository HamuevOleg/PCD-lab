class Interval implements Runnable {
    int start;
    int end;
    boolean flag;
    Thread threadToWaitFor;
    String text;

    /**
     * @param start           Start of the interval.
     * @param end             End of the interval.
     * @param flag            true for forward iteration, false for backward iteration.
     * @param threadToWaitFor The thread to wait for. Pass null if no wait is needed.
     * @param text            The text to be printed after the task is done.
     */
    Interval(int start, int end, boolean flag, Thread threadToWaitFor, String text) {
        this.start = start;
        this.end = end;
        this.flag = flag;
        this.threadToWaitFor = threadToWaitFor;
        this.text = text;
    }

    @Override
    public void run() {
        System.out.println("\n[INFO] Thread for '" + text + "' HAS STARTED.");
        if (flag) {
            for (int i = start; i <= end; i++) {
                System.out.print(i + " ");
            }
        } else {
            for (int i = end; i >= start; i--) {
                System.out.print(i + " ");
            }
        }
        System.out.println("\n[INFO] Thread for '" + text + "' FINISHED loop.");

        while (threadToWaitFor != null && threadToWaitFor.isAlive()) {
            try {
                System.out.println("\n[INFO] Thread '" + text + "' IS WAITING for thread '" + threadToWaitFor.getName() + "'...");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.print("\n[INFO] Thread '" + text + "' is printing: ");
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n[INFO] Thread for '" + text + "' HAS FINISHED.");
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {

        /* Bro, here u have to implement your code
            u need to create 2 threads and start them
            u can read all my comments in the code for
            understanding how our Interval class works,
            that's all
        */

        Runnable task1 = new Interval(1, 10, true, null, "Name (Task 1)");
        Runnable task2 = new Interval(100, 110, true, null, "Surname (Task 2)");

        Thread th1 = new Thread(task1, "Th1");
        Thread th2 = new Thread(task2, "Th2");

        String textFor3 = "Concurrent and Distributed Programming (Task 3)";
        Runnable task3 = new Interval(222, 999, true, th1, textFor3);

        Thread th3 = new Thread(task3, "Th3");

        String textFor4 = "Group FCIM-CR-233 (Task 4)";
        Runnable task4 = new Interval(3333, 3999, false, th3, textFor4);


        Thread th4 = new Thread(task4, "Th4");

        System.out.println("====== STARTING ALL THREADS ======");
        th1.start();
        th2.start();
        th3.start();
        th4.start();

        th1.join();
        th2.join();
        th3.join();
        th4.join();

//        System.out.println("\n====== ALL THREADS FINISHED. Main() is exiting. ======");
    }
}