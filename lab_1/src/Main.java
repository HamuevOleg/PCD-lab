import java.math.BigInteger;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

class NumberMath {

    ArrayList <BigInteger> a;
    ArrayList <BigInteger> b;
    JTextArea outputArea;

    NumberMath(ArrayList <BigInteger> a, ArrayList <BigInteger> b, JTextArea outputArea){
        this.a = a;
        this.b = b;
        this.outputArea = outputArea;
    }

    final Random rand = new Random();

    int random_number_generation(){
        return rand.nextInt(101) * 2;
    }

    void log(String message) {
        System.out.println(message); // Вывод в консоль
        SwingUtilities.invokeLater(() -> {
            outputArea.append(message + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    BigInteger array_a_sum_of_pair_products() {
        ArrayList<BigInteger> evenNumbers = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                evenNumbers.add(a.get(i));
            }
        }

        if (evenNumbers.size() % 2 != 0) {
            log("WARNING: Array A has odd number of even elements. Removing last element: " + evenNumbers.getLast());
            evenNumbers.removeLast();
        }

        BigInteger sum = BigInteger.ZERO;
        int pairCounter = 0;
        int halfSize = evenNumbers.size() / 2;

        for (int i = 0; i < halfSize; i++) {
            BigInteger num1 = evenNumbers.get(i);
            BigInteger num2 = evenNumbers.get(i + halfSize);

            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairSum = num1.add(num2);
            log(String.format("A-%s %s %s %s %d %d", groupName, num1, num2, pairSum, i, i + halfSize));

            BigInteger p = num1.multiply(num2);
            sum = sum.add(p);
            pairCounter++;
        }
        return sum;
    }

    BigInteger array_b_sum_of_pair_products() {
        ArrayList<BigInteger> evenNumbers = new ArrayList<>();
        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                evenNumbers.add(b.get(i));
            }
        }

        if (evenNumbers.size() % 2 != 0) {
            log("WARNING: Array B has odd number of even elements. Removing last element: " + evenNumbers.getLast());
            evenNumbers.removeLast();
        }

        BigInteger sum = BigInteger.ZERO;
        int pairCounter = 0;
        int halfSize = evenNumbers.size() / 2;

        for (int i = 0; i < halfSize; i++) {
            BigInteger num1 = evenNumbers.get(i);
            BigInteger num2 = evenNumbers.get(i + halfSize);

            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairSum = num1.add(num2);
            log(String.format("B-%s %s %s %s %d %d", groupName, num1, num2, pairSum, i, i + halfSize));

            BigInteger p = num1.multiply(num2);
            sum = sum.add(p);
            pairCounter++;
        }
        return sum;
    }

    BigInteger array_a_difference_of_pair_products() {
        ArrayList<BigInteger> evenNumbers = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                evenNumbers.add(a.get(i));
            }
        }

        if (evenNumbers.size() % 2 != 0) {
            log("WARNING: Array A has odd number of even elements. Removing last element: " + evenNumbers.getLast());
            evenNumbers.removeLast();
        }

        BigInteger result = BigInteger.ZERO;
        int pairCounter = 0;
        int halfSize = evenNumbers.size() / 2;

        for (int i = 0; i < halfSize; i++) {
            BigInteger num1 = evenNumbers.get(i);
            BigInteger num2 = evenNumbers.get(i + halfSize);


            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairDifference = num1.subtract(num2);
            log(String.format("A-%s %s %s %s %d %d", groupName, num1, num2, pairDifference, i, i + halfSize));

            BigInteger p = num1.multiply(num2);
            result = result.subtract(p);
            pairCounter++;
        }
        return result;
    }

    BigInteger array_b_difference_of_pair_products() {
        ArrayList<BigInteger> evenNumbers = new ArrayList<>();
        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                evenNumbers.add(b.get(i));
            }
        }

        if (evenNumbers.size() % 2 != 0) {
            log("WARNING: Array B has odd number of even elements. Removing last element: " + evenNumbers.getLast());
            evenNumbers.removeLast();
        }

        BigInteger result = BigInteger.ZERO;
        int pairCounter = 0;
        int halfSize = evenNumbers.size() / 2;

        for (int i = 0; i < halfSize; i++) {
            BigInteger num1 = evenNumbers.get(i);
            BigInteger num2 = evenNumbers.get(i + halfSize);

            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairDifference = num1.subtract(num2);
            log(String.format("B-%s %s %s %s %d %d", groupName, num1, num2, pairDifference, i, i + halfSize));

            BigInteger p = num1.multiply(num2);
            result = result.subtract(p);
            pairCounter++;
        }
        return result;
    }

    BigInteger difference_of_array_a_and_b() {
        BigInteger sum_a = array_a_sum_of_pair_products();
        BigInteger sum_b = array_b_sum_of_pair_products();
        return sum_a.subtract(sum_b);
    }

    BigInteger sum_of_array_a_and_b() {
        BigInteger sum_a = array_a_difference_of_pair_products();
        BigInteger sum_b = array_b_difference_of_pair_products();
        return sum_a.add(sum_b);
    }
}

class MainGUI extends JFrame {
    private final JTextArea outputArea;
    private final JButton startButton;
    private final JLabel statusLabel;

    public MainGUI() {
        setTitle("NumberMath - Variant 9 - Hamuev Oleg");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        // #002060
        Color primaryColor = new Color(0, 32, 96);
        mainPanel.setBackground(primaryColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        JLabel titleLabel = new JLabel("NumberMath Calculator - Variant 9");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(primaryColor);

        startButton = new JButton("Start Calculation");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setPreferredSize(new Dimension(200, 45));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton clearButton = new JButton("Clear Output");
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setPreferredSize(new Dimension(200, 45));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(startButton);
        buttonPanel.add(clearButton);


        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setBackground(new Color(240, 248, 255));
        outputArea.setBorder(BorderFactory.createLineBorder(primaryColor, 2));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(primaryColor);
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(Color.WHITE);
        statusPanel.add(statusLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(primaryColor);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startCalculation());
        clearButton.addActionListener(e -> {
            outputArea.setText("");
            statusLabel.setText("Output cleared");
        });
    }

    private void startCalculation() {
        startButton.setEnabled(false);
        statusLabel.setText("Calculating...");
        outputArea.setText("");

        new Thread(() -> {
            try {
                ArrayList<BigInteger> arrayA = generateRandomArray();
                ArrayList<BigInteger> arrayB = generateRandomArray();

                NumberMath math = new NumberMath(arrayA, arrayB, outputArea);

                final BigInteger[] results = new BigInteger[2];

                outputArea.append("=====================HAMUEV OLEG S-T-A-R-T=====================\n");
                outputArea.append("Variant 9 - Hamuev Oleg - START\n\n");
                outputArea.append("Array A: " + arrayA + "\n\n");
                outputArea.append("Array B: " + arrayB + "\n\n");

                System.out.println("=====================HAMUEV OLEG S-T-A-R-T=====================");
                System.out.println("Variant 9 - Hamuev Oleg - START\n");
                System.out.println("Array A: " + arrayA + "\n");
                System.out.println("Array B: " + arrayB + "\n");

                Runnable taskA = () -> {
                    results[0] = math.array_a_sum_of_pair_products();
                    math.log("Thread for Array A finished calculation.");
                };

                Runnable taskB = () -> {
                    results[1] = math.array_b_sum_of_pair_products();
                    math.log("Thread for Array B finished calculation.");
                };

                Thread threadA = new Thread(taskA);
                Thread threadB = new Thread(taskB);

                threadA.start();
                threadB.start();

                threadA.join();
                threadB.join();

                BigInteger sumA = results[0];
                BigInteger sumB = results[1];
                BigInteger difference = math.difference_of_array_a_and_b();
                BigInteger sum = math.sum_of_array_a_and_b();

                outputArea.append("\n=== RESULTS ===\n");
                outputArea.append("Sum of pair products in A: " + sumA + "\n");
                outputArea.append("Sum of pair products in B: " + sumB + "\n");
                outputArea.append("Difference(PART-1): " + difference + "\n");
                outputArea.append("Sum(PART-2): " + sum + "\n");
                outputArea.append("\nVariant 9 - Hamuev Oleg - END\n");
                outputArea.append("=====================HAMUEV OLEG E-N-D=====================\n");


                System.out.println("\n=== RESULTS ===");
                System.out.println("Sum of pair products in A: " + sumA);
                System.out.println("Sum of pair products in B: " + sumB);
                System.out.println("Difference(PART-1): " + difference);
                System.out.println("Sum(PART-2): " + sum);
                System.out.println("\nVariant 9 - Hamuev Oleg - END");
                System.out.println("=====================HAMUEV OLEG E-N-D=====================");

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Calculation completed!");
                    startButton.setEnabled(true);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("\nERROR: " + ex.getMessage() + "\n");
                    System.out.println("\nERROR: " + ex.getMessage());
                    statusLabel.setText("Error occurred");
                    startButton.setEnabled(true);
                });
            }
        }).start();
    }

    private ArrayList<BigInteger> generateRandomArray() {
        Random rand = new Random();
        ArrayList<BigInteger> result = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            result.add(BigInteger.valueOf(rand.nextInt(101)));
        }
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}