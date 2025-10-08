import java.util.Random;


class Th1 implements Runnable {
    private int[] mas;
    private Thread thread;

    public Th1(int[] mas, String name) {
        this.mas = mas;
        this.thread = new Thread(this, name);
    }

    public void start() {
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " начал работу");

        int totalSum = 0;


        for (int i = 1; i < mas.length - 2; i += 4) {
            if (i + 2 < mas.length) {
                int pos1 = i;
                int pos2 = i + 2;
                int product = mas[pos1] * mas[pos2];
                totalSum += product;

                System.out.println(Thread.currentThread().getName() +
                        " | Позиции: [" + pos1 + ", " + pos2 + "]" +
                        " | Числа: " + mas[pos1] + " * " + mas[pos2] +
                        " = " + product +
                        " | Текущая сумма: " + totalSum);
            }
        }

        System.out.println(Thread.currentThread().getName() +
                " завершил работу | ИТОГОВАЯ СУММА: " + totalSum);
    }
}


class Th2 implements Runnable {
    private int[] mas;
    private Thread thread;

    public Th2(int[] mas, String name) {
        this.mas = mas;
        this.thread = new Thread(this, name);
    }

    public void start() {
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " начал работу");

        int totalSum = 0;


        int lastOdd = (mas.length % 2 == 0) ? mas.length - 1 : mas.length - 2;


        for (int i = lastOdd; i >= 3; i -= 4) {
            if (i - 2 >= 1) {
                int pos1 = i;
                int pos2 = i - 2;
                int product = mas[pos1] * mas[pos2];
                totalSum += product;

                System.out.println(Thread.currentThread().getName() +
                        " | Позиции: [" + pos1 + ", " + pos2 + "]" +
                        " | Числа: " + mas[pos1] + " * " + mas[pos2] +
                        " = " + product +
                        " | Текущая сумма: " + totalSum);
            }
        }

        System.out.println(Thread.currentThread().getName() +
                " завершил работу | ИТОГОВАЯ СУММА: " + totalSum);
    }
}

public class Main {
    public static void main(String[] args) {
        // Генерируем массив
        int[] mas = new int[100];
        Random rand = new Random();

        System.out.println("Сгенерированный массив:");
        for (int i = 0; i < 100; i++) {
            mas[i] = rand.nextInt(100) + 1; // числа от 1 до 100
            System.out.print(mas[i] + " ");
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println("\n");


        Th1 th1 = new Th1(mas, "Поток_1");
        Th2 th2 = new Th2(mas, "Поток_2");

        th1.start();
        th2.start();


        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== Все потоки завершены ===\n");

        // Вывод информации о студентах с задержкой
        String studentInfo = "Лабораторную работу выполнили: Хамуев Олег, Дрига Даниил";
        for (char c : studentInfo.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}