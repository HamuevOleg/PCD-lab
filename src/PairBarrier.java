public class PairBarrier {
    /**
     *
     * Why lock is Object TYPE DATA --
     * --
     * This object serves as a dedicated "monitor" or "lock" for synchronization.
     * We use it to control access to the 'finishedCount' variable and to
     * manage the waiting/notifying of threads.
     *
     * Why 'Object'?:
     * 1. LIGHTWEIGHT: 'Object' is the simplest, most lightweight class in Java.
     * Its *only* purpose here is to be a lock, carrying no extra data or methods.
     *
     * 2. SAFETY (UNIQUENESS): We use 'new Object()' to guarantee we have a
     * brand-new, unique object instance. Using other types like 'String' or
     * 'Integer' is dangerous because the JVM might cache or "intern" them.
     * This could cause unrelated parts of your program to accidentally
     * grab the *same* lock, leading to a deadlock.
     * --
     */
    final Object lock = new Object();
    int finishedCount = 0; // needed for synchronization

    void signalAndWait() {
        synchronized (lock) {
            finishedCount++;

            if (finishedCount < 2) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is waiting for its partner...");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " is notifying its partner...");
                lock.notify();
            }
        }
    }
}