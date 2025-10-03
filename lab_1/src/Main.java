import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

class NumberDifference {

    ArrayList<Integer> a;
    ArrayList<Integer> b;

    NumberDifference(ArrayList<Integer> a, ArrayList<Integer> b) {
        this.a = a;
        this.b = b;
    }

    BigInteger getProduct() {
        BigInteger product = BigInteger.ONE;
        for (int number : a) {
            if (number == 0) return BigInteger.ZERO;
            product = product.multiply(BigInteger.valueOf(number));
        }
        return product;
    }

    BigInteger getProduct2() {
        BigInteger product = BigInteger.ONE;
        for (int number : b) {
            if (number == 0) return BigInteger.ZERO;
            product = product.multiply(BigInteger.valueOf(number));
        }
        return product;
    }

    BigInteger getDifference() {
        return getProduct().subtract(getProduct2());
    }
}

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            numbers.add(random.nextInt(101));
        }

        System.out.println("Result:");
        for (int i = 0; i < numbers.size(); i++) {
            System.out.print(numbers.get(i) + (i % 20 == 19 ? "\n" : " "));
        }
        System.out.println();

        ArrayList<Integer> groupOne = new ArrayList<>();
        ArrayList<Integer> groupTwo = new ArrayList<>();

        ArrayList<Integer> tempPair = new ArrayList<>();
        ArrayList<Integer> tempIndices = new ArrayList<>();
        boolean isGroupOneTurn = true;

        for (int i = 0; i < numbers.size(); i++) {
            int currentNumber = numbers.get(i);
            if (currentNumber % 2 == 0) {
                tempPair.add(currentNumber);
                tempIndices.add(i);

                if (tempPair.size() == 2) {
                    int num1 = tempPair.get(0);
                    int num2 = tempPair.get(1);
                    int index1 = tempIndices.get(0);
                    int index2 = tempIndices.get(1);
                    String groupName;

                    if (isGroupOneTurn) {
                        groupName = "First";
                        groupOne.add(num1);
                        groupOne.add(num2);
                    } else {
                        groupName = "Second";
                        groupTwo.add(num1);
                        groupTwo.add(num2);
                    }
                    System.out.printf("%s %d %d %d %d %d\n", groupName, num1, num2, num1 + num2, index1, index2);

                    isGroupOneTurn = !isGroupOneTurn;
                    tempPair.clear();
                    tempIndices.clear();
                }
            }
        }

        NumberDifference calculator = new NumberDifference(groupOne, groupTwo);
        BigInteger difference = calculator.getDifference();

        System.out.println("\n First Product group: " + calculator.getProduct());
        System.out.println("\n Second Product group: " + calculator.getProduct2());
        System.out.println("\n Difference of product: " + difference);
    }
}