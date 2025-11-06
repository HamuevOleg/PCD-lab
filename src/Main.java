import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        RandomArray randGen1 = new RandomArray(100, 101);
        RandomArray randGen2 = new RandomArray(100, 101);

        ArrayList<Integer> list1 = randGen1.randomise_value();
        ArrayList<Integer> list2 = randGen2.randomise_value();

        CountDownLatch latch = new CountDownLatch(4);
        CyclicBarrier barrier = new CyclicBarrier(4);

        // Sequence : Th2 -> Th4 -> Th1 -> Th3
        ArrayList<Integer> executionOrder = new ArrayList<>();
        executionOrder.add(2); executionOrder.add(4); executionOrder.add(1); executionOrder.add(3);

        TurnManager calcManager  = new TurnManager(executionOrder); // For calcs
        TurnManager printManager = new TurnManager(executionOrder); // For prints

        // Th1 (Task 1, flag=true, использует list1)
        TaskOneAndTwo task1 = new TaskOneAndTwo("Hamuev | Drыga", true, list1,
                latch, calcManager, printManager, barrier, 1); // ID = 1

        // Th2 (Task 2, flag=false, использует list2)
        TaskOneAndTwo task2 = new TaskOneAndTwo("OLEG | Daniil", false, list2,
                latch, calcManager, printManager, barrier, 2); // ID = 2

        // Th3 (Task 3, flag=true)
        TaskThreeAndFour task3 = new TaskThreeAndFour("Конкурентное программирование", true,
                latch, calcManager, printManager, barrier, 3, 333, 999); // ID = 3

        // Th4 (Task 4, flag=false)
        TaskThreeAndFour task4 = new TaskThreeAndFour("CR-233", false,
                latch, calcManager, printManager, barrier, 4, 9999, 3333); // ID = 4


        Thread th1 = new Thread(task1);
        Thread th2 = new Thread(task2);
        Thread th3 = new Thread(task3);
        Thread th4 = new Thread(task4);

        th1.start(); th2.start(); th3.start(); th4.start();

        latch.await();
        System.out.println("--- Main: Все потоки завершили работу ---");
    }
}