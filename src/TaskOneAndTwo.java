import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier; // 🔥 Импортируем барьер

public class TaskOneAndTwo implements Runnable {
    boolean whichTaskIs; // true 1 or 2; false 3 or 4
    String text;
    boolean flag;
    int priority;
    ArrayList<Integer> array;

    // Синхронизаторы
    CountDownLatch latch;
    TurnManager calcManager;
    TurnManager printManager;
    CyclicBarrier barrier;
    int myId;

    /**
     * @param text         - Текст для Фазы 2 (Имя, Фамилия, и т.д.)
     * @param flag         - for choosing the task : one or two
     * @param array        - here is our data stored
     * @param latch        - для main
     * @param calcManager  - Менеджер очереди для расчетов
     * @param printManager - Менеджер очереди для печати
     * @param barrier      - Барьер для ожидания
     * @param myId         - уникальный ID этого потока
     **/
    TaskOneAndTwo(String text, boolean flag, int priority, ArrayList<Integer> array,
                  CountDownLatch latch, TurnManager calcManager, TurnManager printManager,
                  CyclicBarrier barrier, int myId) {
        this.flag = flag;
        this.text = text;
        this.array = array;
        this.priority = priority;
        this.latch = latch;
        this.calcManager = calcManager;
        this.printManager = printManager;
        this.barrier = barrier;
        this.myId = myId;
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


            int sum = 0;

            // if first_part - true -> task 1 or 2

            ArrayList<Integer> tmp_array = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i) % 2 == 0) {
                    tmp_array.add(array.get(i));
                }
            }
            if (flag) {
                for (int i = 0; i < tmp_array.size(); i++) {
                    sum += tmp_array.get(i);
                }
            } else {
                for (int i = tmp_array.size() - 1; i >= 0; i--) {
                    sum += tmp_array.get(i);
                }
            }
            System.out.println("Поток " + myId + " (" + text.split(" ")[0] + "): ЗАВЕРШИЛ РАСЧЕТ, sum = " + sum);

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