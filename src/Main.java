//Main
//├── 🟡 G6
//│   ├── ThA ..................... Priority: 3
//│   ├── Th1 ..................... Priority: 4
//│   └── Th2 ..................... Priority: 3
//│
//└── 🟢 G2
//├── Th1 ..................... Priority: 2
//├── 🔵 G3
//│   ├── Tha ................. Priority: 2
//│   ├── Thb ................. Priority: 3
//│   ├── Thc ................. Priority: 4 ⭐ Max
//│   └── Thd ................. Priority: 3
//├── Th2 ..................... Priority: 3
//└── Th3 ..................... Priority: 3

class Custom_thread extends Thread{
    Custom_thread(ThreadGroup group, String name, int priority) {
        super(group,name);
        setPriority(priority);
        setDaemon(true);
    }

    public void run() {
        System.out.println(
                "Thread '" + getName() +
                        "' from Group '" + getThreadGroup().getName() +
                        "' started with priority " + getPriority()
        );
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup main = new ThreadGroup("main");
            ThreadGroup g6 = new ThreadGroup(main, "g6");
            ThreadGroup g2 = new ThreadGroup(main, "g2");
            ThreadGroup g3 = new ThreadGroup(g2, "g3");

            /*G6 group */
            Thread ThA = new Custom_thread(g6, "ThA", 3);
            Thread Th1 = new Custom_thread(g6, "Th1", 4);
            Thread Th2 = new Custom_thread(g6, "Th2", 3);

            /*G2 group */
            Thread Th1_g2 = new Custom_thread(g2, "Th1", 2);
                /*G3 group in G2*/
                Thread Tha = new Custom_thread(g3, "Tha", 2);
                Thread Thb = new Custom_thread(g3, "Thb", 3);
                Thread Thc = new Custom_thread(g3, "Thc", 4);
                Thread Thd = new Custom_thread(g3, "Thd", 3);
            //test for//
            Thread Th2_g2 = new Custom_thread(g2, "Th2", 3);
            Thread Th3 = new Custom_thread(g2, "Th3", 3);

        ThA.start(); Th1.start(); Th2.start(); Th1_g2.start(); Tha.start(); Thb.start(); Thc.start(); Thd.start(); Th2_g2.start(); Th3.start();

        ThA.join(); Th1.join(); Th2.join(); Th1_g2.join(); Tha.join(); Thb.join(); Thc.join(); Thd.join(); Th2_g2.join(); Th3.join();

        main.list();
        System.out.println("Total active threads is " +
        main.activeCount());


    }
}