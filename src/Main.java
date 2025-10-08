import java.util.Random;
import javax.swing.*;
import java.awt.*;

// Первый поток - суммирует произведения чисел на нечетных позициях с начала
class Th1 implements Runnable {
    private int[] mas;
    private Thread thread;
    private JTextArea outputArea;

    public Th1(int[] mas, String name, JTextArea outputArea) {
        this.mas = mas;
        this.outputArea = outputArea;
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
        appendText(Thread.currentThread().getName() + " начал работу\n");

        int totalSum = 0;

        for (int i = 1; i < mas.length - 2; i += 4) {
            if (i + 2 < mas.length) {
                int pos1 = i;
                int pos2 = i + 2;
                int product = mas[pos1] * mas[pos2];
                totalSum += product;

                appendText(Thread.currentThread().getName() +
                        " | Позиции: [" + pos1 + ", " + pos2 + "]" +
                        " | Числа: " + mas[pos1] + " * " + mas[pos2] +
                        " = " + product +
                        " | Сумма: " + totalSum + "\n");

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        appendText(Thread.currentThread().getName() +
                " завершил работу | ИТОГОВАЯ СУММА: " + totalSum + "\n\n");
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> outputArea.append(text));
    }
}

// Второй поток - суммирует произведения чисел на нечетных позициях с конца
class Th2 implements Runnable {
    private int[] mas;
    private Thread thread;
    private JTextArea outputArea;

    public Th2(int[] mas, String name, JTextArea outputArea) {
        this.mas = mas;
        this.outputArea = outputArea;
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
        appendText(Thread.currentThread().getName() + " начал работу\n");

        int totalSum = 0;
        int lastOdd = (mas.length % 2 == 0) ? mas.length - 1 : mas.length - 2;

        for (int i = lastOdd; i >= 3; i -= 4) {
            if (i - 2 >= 1) {
                int pos1 = i;
                int pos2 = i - 2;
                int product = mas[pos1] * mas[pos2];
                totalSum += product;

                appendText(Thread.currentThread().getName() +
                        " | Позиции: [" + pos1 + ", " + pos2 + "]" +
                        " | Числа: " + mas[pos1] + " * " + mas[pos2] +
                        " = " + product +
                        " | Сумма: " + totalSum + "\n");

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        appendText(Thread.currentThread().getName() +
                " завершил работу | ИТОГОВАЯ СУММА: " + totalSum + "\n\n");
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> outputArea.append(text));
    }
}

public class Main extends JFrame {
    private JTextArea arrayArea;
    private JTextArea outputArea;
    private JButton startButton;
    private int[] mas;

    public Main() {
        setTitle("Лабораторная работа №1 - Вариант 6");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Верхняя панель с заголовком
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Многопоточная обработка массива");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Панель для массива
        JPanel arrayPanel = new JPanel(new BorderLayout());
        arrayPanel.setBorder(BorderFactory.createTitledBorder("Сгенерированный массив"));
        arrayArea = new JTextArea(6, 50);
        arrayArea.setEditable(false);
        arrayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        arrayArea.setLineWrap(true);
        arrayArea.setWrapStyleWord(true);
        JScrollPane arrayScroll = new JScrollPane(arrayArea);
        arrayPanel.add(arrayScroll, BorderLayout.CENTER);

        // Панель для вывода результатов
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Результаты работы потоков"));
        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        // Центральная панель
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(arrayPanel);
        centerPanel.add(outputPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Нижняя панель с кнопкой
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        startButton = new JButton("Запустить потоки");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setPreferredSize(new Dimension(200, 40));
        startButton.setBackground(new Color(60, 179, 113));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> runThreads());
        bottomPanel.add(startButton);
        add(bottomPanel, BorderLayout.SOUTH);

        generateArray();
    }

    private void generateArray() {
        mas = new int[100];
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            mas[i] = rand.nextInt(100);
            sb.append(mas[i]).append(" ");
            if ((i + 1) % 10 == 0) sb.append("\n");
        }

        arrayArea.setText(sb.toString());
    }

    private void runThreads() {
        startButton.setEnabled(false);
        outputArea.setText("");

        new Thread(() -> {
            Th1 th1 = new Th1(mas, "Поток_1", outputArea);
            Th2 th2 = new Th2(mas, "Поток_2", outputArea);

            th1.start();
            th2.start();

            try {
                th1.join();
                th2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                outputArea.append("\n--- Все потоки завершены ---\n\n");

                String studentInfo = "Лабораторную работу выполнили: Дрига Даниил, Хамуев Олег. В простонародье: мама Италия папа Бразилия";
                new Thread(() -> {
                    for (char c : studentInfo.toCharArray()) {
                        final String ch = String.valueOf(c);
                        SwingUtilities.invokeLater(() -> outputArea.append(ch));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    SwingUtilities.invokeLater(() -> {
                        outputArea.append("\n");
                        startButton.setEnabled(true);
                    });
                }).start();
            });
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setVisible(true);
        });
    }
}