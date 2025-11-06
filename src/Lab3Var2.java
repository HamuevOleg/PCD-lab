import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Lab3Var2 {
    public static void main(String[] args) throws InterruptedException {
        RandomArray randGen1 = new RandomArray(100, 101);
        RandomArray randGen2 = new RandomArray(100, 101);

        ArrayList<Integer> list1 = randGen1.randomise_value();
        ArrayList<Integer> list2 = randGen2.randomise_value();

        CountDownLatch latch = new CountDownLatch(4);

        CyclicBarrier barrier = new CyclicBarrier(4);

        ArrayList<Integer> executionOrder = new ArrayList<>();
        executionOrder.add(2); executionOrder.add(4); executionOrder.add(1); executionOrder.add(3);

        // 🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥
        TurnManager calcManager  = new TurnManager(executionOrder);
        TurnManager printManager = new TurnManager(executionOrder);

        TurnManager turnManager = new TurnManager(executionOrder);

        // Мы передаем 'turnManager' и уникальный ID (1, 2, 3, 4) в каждый таск
        TaskOneAndTwo task1 = new TaskOneAndTwo("Olegos (Фамилия)", true, 1, list1,
                latch, calcManager, printManager, barrier, 1); // ID = 1

        TaskOneAndTwo task2 = new TaskOneAndTwo("Hamuev (Имя)", false, 2, list2,
                latch, calcManager, printManager, barrier, 2); // ID = 2

        TaskOneAndTwo task3 = new TaskOneAndTwo("Конкурентное программирование (Дисциплина)", true, 1, list1,
                latch, calcManager, printManager, barrier, 3); // ID = 3

        TaskOneAndTwo task4 = new TaskOneAndTwo("FI-211 (Группа)", false, 2, list2,
                latch, calcManager, printManager, barrier, 4);

        Thread th1 = new Thread(task1);
        Thread th2 = new Thread(task2);
        Thread th3 = new Thread(task3);
        Thread th4 = new Thread(task4);

        th1.start(); th2.start(); th3.start(); th4.start();

        latch.await();
    }
}