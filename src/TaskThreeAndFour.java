import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class TaskThreeAndFour implements Runnable {
    String text;
    boolean flag; // true = Task 3 (Вариант 9), false = Task 4 (Вариант 9)

    // Синхронизаторы
    CountDownLatch latch;
    TurnManager calcManager;
    TurnManager printManager;
    CyclicBarrier barrier;
    int myId;
    int min;
    int max;

    /**
     * @param text         - Текст для Фазы 2 (Дисциплина, Группа)
     * @param flag         - true для Задачи 3, false для Задачи 4
     * @param latch        - для main
     * @param calcManager  - Менеджер очереди для расчетов
     * @param printManager - Менеджер очереди для печати
     * @param barrier      - Барьер для ожидания
     * @param myId         - уникальный ID этого потока
     **/
    TaskThreeAndFour(String text, boolean flag,
                     CountDownLatch latch, TurnManager calcManager, TurnManager printManager,
                     CyclicBarrier barrier, int myId, int min, int max) {
        this.flag = flag;
        this.text = text;
        this.latch = latch;
        this.calcManager = calcManager;
        this.printManager = printManager;
        this.barrier = barrier;
        this.myId = myId;
        this.min = min;
        this.max = max;
    }

    private void printTextWithDelay(String text) throws InterruptedException {
        System.out.print("Поток " + myId + " печатает: ");
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println();
    }

    @Override
    public void run() {
        try {
            calcManager.waitForTurn(myId);

            System.out.println("Поток " + myId + " (" + text.split(" ")[0] + ") начинает итерацию:");

            if (flag) {
                for (int i = min; i <= max; i++) {

                    if (i % 100 == 0) Thread.yield();
                }
            } else {
                for (int i = min; i >= max; i--) {

                    if (i % 100 == 0) Thread.yield();
                }
            }


            System.out.println("\nПоток " + myId + " (" + text.split(" ")[0] + "): ЗАВЕРШИЛ РАСЧЕТ (итерация).");

            calcManager.nextTurn();

            barrier.await();

            printManager.waitForTurn(myId);
            printTextWithDelay(this.text);
            printManager.nextTurn();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}