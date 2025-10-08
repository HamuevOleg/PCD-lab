import java.util.Random;


class Th1 implements Runnable {
    private int[] mas;
    private Thread thread;
    private int from;
    private int to;
    private int step;

    public Th1(int[] mas, int from, int to, int step, String name) {
        this.mas = mas;
        this.from = from;
        this.to = to;
        this.step = step;
        this.thread = new Thread(this, name);
    }

    public void start() {
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    public void run() {
        int i = from;
        while (i != to) {
            if (i >= 0 && i < mas.length && mas[i] <= 50) {
                int firstIndex = i;
                i += step;
                while (true) {
                    if (i < 0 || i >= mas.length) {
                        break;
                    }
                    if (mas[i] <= 50) {
                        int secondIndex = i;
                        int sumOfIndices = firstIndex + secondIndex;
                        System.out.println(Thread.currentThread().getName() + " " + firstIndex + " " + secondIndex + " " + sumOfIndices + " " + mas[firstIndex] + " " + mas[secondIndex]);
                        break;
                    }
                    i += step;
                }
            }
            i += step;
        }
    }
}


class Th2 implements Runnable {
    private int[] mas;
    private Thread thread;
    private int from;
    private int to;
    private int step;

    public Th2(int[] mas, int from, int to, int step, String name) {
        this.mas = mas;
        this.from = from;
        this.to = to;
        this.step = step;
        this.thread = new Thread(this, name);
    }

    public void start() {
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    public void run() {
        int i = from;
        while (i != to) {
            if (i >= 0 && i < mas.length && mas[i] <= 50) {
                int firstIndex = i;
                i += step;
                while (true) {
                    if (i < 0 || i >= mas.length) {
                        break;
                    }
                    if (mas[i] <= 50) {
                        int secondIndex = i;
                        int sumOfIndices = firstIndex + secondIndex;
                        System.out.println(Thread.currentThread().getName() + " " + firstIndex + " " + secondIndex + " " + sumOfIndices + " " + mas[firstIndex] + " " + mas[secondIndex]);
                        break;
                    }
                    i += step;
                }
            }
            i += step;
        }
    }
}


public class Main {
    public static void main(String[] args) {

        int[] mas = new int[101];
        Random rand = new Random();
        System.out.println("Результат выполнения:");
        for (int i = 0; i < 100; i++) {
            mas[i] = rand.nextInt(99); // 0..98
            System.out.print(mas[i] + " ");
        }
        System.out.println(" ");


        Th1 th1 = new Th1(mas, 0, 99, 1, "Один");
        Th2 th2 = new Th2(mas, 99, 0, -1, "Два");

        th1.start();
        th2.start();


        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String studentInfo = "Лабораторную работу выполнили: Хамуев Олег (Kylian Mbappe), Дрига Даниил (Igor Akinfeev)";
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