import java.math.BigInteger;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

class NumberMath {

    ArrayList <BigInteger> a;
    ArrayList <BigInteger> b;
    JTextArea outputArea;
    String threadName;

    NumberMath(ArrayList <BigInteger> a, ArrayList <BigInteger> b, JTextArea outputArea, String threadName){
        this.a = a;
        this.b = b;
        this.outputArea = outputArea;
        this.threadName = threadName;
    }

    void log(String message) {
        System.out.println(message);
        SwingUtilities.invokeLater(() -> {
            outputArea.append(message + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    private BigInteger processPairProducts(ArrayList<BigInteger> array, boolean flag, String threadName) {
        ArrayList<BigInteger> evenNumbers = new ArrayList<>();
        for (BigInteger num : array) {
            // num % 2 = 0 if true -> array + element
            if (num.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                evenNumbers.add(num);
            }
        }

        BigInteger result = BigInteger.ZERO;
        int pairCounter = 0;
        int processedElements = 0;
        ArrayList<BigInteger> remainingArray = new ArrayList<>(evenNumbers);

        for (int i = 0; i + 3 < evenNumbers.size(); i += 4) {
            BigInteger val1 = evenNumbers.get(i);
            BigInteger val2 = evenNumbers.get(i + 1);
            BigInteger val3 = evenNumbers.get(i + 2);
            BigInteger val4 = evenNumbers.get(i + 3);

            BigInteger pairProduct;

            pairProduct = flag ? val1.multiply(val3) : val2.multiply(val4);

            result = flag ? result.add(pairProduct) : result.subtract(pairProduct);
            pairCounter++;
            processedElements = i + 4;

            for (int j = 0; j < 4; j++) {
                if (!remainingArray.isEmpty())
                    remainingArray.removeFirst();
            }

            String message = threadName + " - [" + result + "] - " + remainingArray;
            log(message);
        }

        int remain = evenNumbers.size() - processedElements;

        //1 element in array
        if (remain == 1) {
            BigInteger val = evenNumbers.get(processedElements);
            result = flag ? result.add(val) : result.subtract(val);
            log(threadName + " - One element left: " + val);
        }
        //2 elements in array
        else if (remain == 2) {
            BigInteger val1 = evenNumbers.get(processedElements);
            BigInteger val2 = evenNumbers.get(processedElements + 1);
            BigInteger product = val1.multiply(val2);
            result = flag ? result.add(product) : result.subtract(product);
            log(threadName + " - Two elements left: " + val1 + ", " + val2);
        }
        //3 elements in array
        else if (remain == 3) {
            BigInteger val1 = evenNumbers.get(processedElements);
            BigInteger val2 = evenNumbers.get(processedElements + 1);
            BigInteger val3 = evenNumbers.get(processedElements + 2);
            BigInteger combined;
            if (flag) {
                combined = val1.multiply(val3).add(val2);
            } else {
                combined = val1.multiply(val3).subtract(val2);
            }
            result = flag ? result.add(combined) : result.subtract(combined);
            log(threadName + " - Three elements left: " + val1 + ", " + val2 + ", " + val3);
        }

        log(threadName + " - Final result: " + result);
        return result;
    }

    BigInteger array_a_sum_of_pair_products() {
        return processPairProducts(a, true, threadName);
    }

    BigInteger array_b_sum_of_pair_products() {
        return processPairProducts(b, true, threadName);
    }

    BigInteger array_a_difference_of_pair_products() {
        return processPairProducts(a, false, threadName);
    }

    BigInteger array_b_difference_of_pair_products() {
        return processPairProducts(b, false, threadName);
    }
}

class GradientButton extends JButton {
    private final Color color1;
    private final Color color2;
    private boolean isHovered = false;

    public GradientButton(String text, Color c1, Color c2) {
        super(text);
        this.color1 = c1;
        this.color2 = c2;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp;
        if (isHovered) {
            gp = new GradientPaint(0, 0, color1.brighter(), w, h, color2.brighter());
        } else {
            gp = new GradientPaint(0, 0, color1, w, h, color2);
        }

        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, 15, 15);

        g2.dispose();
        super.paintComponent(g);
    }
}

class GradientPanel extends JPanel {
    private final Color color1;
    private final Color color2;

    public GradientPanel(Color c1, Color c2) {
        this.color1 = c1;
        this.color2 = c2;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }
}

class MainGUI extends JFrame {
    private final JTextArea outputArea;
    private final GradientButton startButton;
    private final JLabel statusLabel;
    private Timer pulseTimer;
    private long startTime;

    public MainGUI() {
        setTitle("NumberMath Calculator - Variant 9");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color darkBlue = new Color(13, 27, 62);
        Color mediumBlue = new Color(27, 38, 79);
        Color accentBlue = new Color(65, 105, 225);

        GradientPanel mainPanel = new GradientPanel(darkBlue, mediumBlue);
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("NumberMath Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Variant 9 - Hamuev Oleg");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(180, 200, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        startButton = new GradientButton("Start Calculation",
                new Color(34, 193, 195), new Color(45, 253, 139));
        startButton.setPreferredSize(new Dimension(220, 50));

        GradientButton clearButton = new GradientButton("Clear Output",
                new Color(252, 70, 107), new Color(63, 94, 251));
        clearButton.setPreferredSize(new Dimension(220, 50));

        buttonPanel.add(startButton);
        buttonPanel.add(clearButton);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setOpaque(false);
        outputPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(65, 105, 225, 100)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        outputArea.setBackground(new Color(20, 30, 48));
        outputArea.setForeground(new Color(230, 237, 243));
        outputArea.setCaretColor(accentBlue);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        outputPanel.add(scrollPane);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setOpaque(false);
        statusLabel = new JLabel("● Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        statusLabel.setForeground(new Color(34, 255, 195));
        statusPanel.add(statusLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(outputPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(statusPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startCalculation());
        clearButton.addActionListener(e -> {
            outputArea.setText("");
            statusLabel.setText("● Output cleared");
            statusLabel.setForeground(new Color(255, 200, 87));
        });
    }

    private void startCalculation() {
        startButton.setEnabled(false);
        statusLabel.setText("Calculating...");
        statusLabel.setForeground(new Color(255, 165, 0));
        outputArea.setText("");

        pulseTimer = new Timer(500, e -> {
            String text = statusLabel.getText();
            if (text.endsWith("...")) {
                statusLabel.setText("⚙ Calculating");
            } else {
                statusLabel.setText(text + ".");
            }
        });
        pulseTimer.start();

        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            try {
                ArrayList<BigInteger> arrayA = generateRandomArray();
                ArrayList<BigInteger> arrayB = generateRandomArray();

                NumberMath mathA = new NumberMath(arrayA, arrayB, outputArea, "Thread ONE");
                NumberMath mathB = new NumberMath(arrayA, arrayB, outputArea, "Thread TWO");

                final BigInteger[] results = new BigInteger[4];

                outputArea.append("║HAMUEV OLEG - CALCULATION START║\n");
                outputArea.append("Array A: " + arrayA + "\n\n");
                outputArea.append("Array B: " + arrayB + "\n\n");
                outputArea.append("Processing...\n\n");

                System.out.println("=====================HAMUEV OLEG S-T-A-R-T=====================");
                System.out.println("Variant 9 - Hamuev Oleg - START\n");
                System.out.println("Array A: " + arrayA + "\n");
                System.out.println("Array B: " + arrayB + "\n");

                Runnable taskA = () -> {
                    results[0] = mathA.array_a_sum_of_pair_products();
                    mathA.log("Thread for Array A finished calculation - PART - 1.");
                };

                Runnable taskB = () -> {
                    results[1] = mathB.array_b_sum_of_pair_products();
                    mathB.log("Thread for Array B finished calculation - PART - 1.");
                };

                Runnable taskC = () -> {
                    results[3] = mathA.array_a_difference_of_pair_products();
                    mathA.log("Thread for Array A finished calculation - PART - 2.");
                };

                Runnable taskD = () -> {
                    results[2] = mathB.array_b_difference_of_pair_products();
                    mathB.log("Thread for Array B finished calculation - PART - 2.");
                };

                Thread threadA = new Thread(taskA);
                Thread threadB = new Thread(taskB);
                Thread threadC = new Thread(taskC);
                Thread threadD = new Thread(taskD);

                threadA.start();
                threadB.start();
                threadC.start();
                threadD.start();

                threadA.join();
                threadB.join();
                threadC.join();
                threadD.join();

                BigInteger sumA = results[0];
                BigInteger sumB = results[1];
                BigInteger diffC = results[2];
                BigInteger diffD = results[3];
                BigInteger difference = sumA.subtract(sumB);
                BigInteger sum = diffC.add(diffD);

                outputArea.append("║RESULTS║\n");
                outputArea.append("Sum of pair products in A: " + sumA + "\n");
                outputArea.append("Sum of pair products in B: " + sumB + "\n");
                outputArea.append("Difference (PART-1): " + difference + "\n\n");
                outputArea.append("Difference of pair products in A: " + diffC + "\n");
                outputArea.append("Difference of pair products in B: " + diffD + "\n");
                outputArea.append("Sum (PART-2): " + sum + "\n\n");
                outputArea.append("║HAMUEV OLEG - CALCULATION END║\n");

                System.out.println("\n=== RESULTS ===\n");
                System.out.println("Sum of pair products in A: " + sumA + "\n");
                System.out.println("Sum of pair products in B: " + sumB + "\n");
                System.out.println("Difference(PART-1): " + difference + "\n");
                System.out.println("Sum(PART-2): " + sum + "\n");
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                outputArea.append("║EXECUTION STATISTICS║\n");
                outputArea.append("Total execution time: " + executionTime + " ms\n");
                System.out.println("\n=== EXECUTION STATISTICS ===\n");
                System.out.println("Total execution time: " + executionTime + " ms\n");
                System.out.println("\nVariant 9 - Hamuev Oleg - END\n");
                System.out.println("=====================HAMUEV OLEG E-N-D=====================\n");

                SwingUtilities.invokeLater(() -> {
                    pulseTimer.stop();
                    statusLabel.setText("Calculation completed!");
                    statusLabel.setForeground(new Color(34, 255, 195));
                    startButton.setEnabled(true);
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    pulseTimer.stop();
                    outputArea.append("\nERROR: " + ex.getMessage() + "\n");
                    System.out.println("\nERROR: " + ex.getMessage());
                    statusLabel.setText("✗ Error occurred");
                    statusLabel.setForeground(new Color(255, 70, 107));
                    startButton.setEnabled(true);
                });
            }
        }).start();
    }

    private ArrayList<BigInteger> generateRandomArray() {
        Random rand = new Random();
        ArrayList<BigInteger> result = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            result.add(BigInteger.valueOf(rand.nextInt(101)));
        }
        return result;
    }

     static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}

class RoundedBorder implements Border {
    private final int radius;
    private final Color color;

    RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}