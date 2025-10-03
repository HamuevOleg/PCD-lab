import java.math.BigInteger;
import java.util.Random;
import java.util.ArrayList;

class NumberMath {

    ArrayList <BigInteger> a;
    ArrayList <BigInteger> b;
    NumberMath(ArrayList <BigInteger> a, ArrayList <BigInteger> b){
        this.a = a;
        this.b = b;
    }

    final Random rand = new Random();

    int random_number_generation(){
        return rand.nextInt(101) * 2;
    }

    BigInteger array_a_sum_of_pair_products() {
        BigInteger sum = BigInteger.ZERO;
        int pairCounter = 0;

        for (int i = 0; i < a.size() - 1; i += 2) {
            BigInteger num1 = a.get(i);
            BigInteger num2 = a.get(i + 1);

            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairSum = num1.add(num2);
            System.out.printf("A-%s %s %s %s %d %d\n", groupName, num1, num2, pairSum, i, i + 1);

            BigInteger p = num1.multiply(num2);
            sum = sum.add(p);
            pairCounter++;
        }
        return sum;
    }

    BigInteger array_b_sum_of_pair_products() {
        BigInteger sum = BigInteger.ZERO;
        int pairCounter = 0;

        for (int i = 0; i < b.size() - 1; i += 2) {
            BigInteger num1 = b.get(i);
            BigInteger num2 = b.get(i + 1);

            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairSum = num1.add(num2);
            System.out.printf("B-%s %s %s %s %d %d\n", groupName, num1, num2, pairSum, i, i + 1);

            BigInteger p = num1.multiply(num2);
            sum = sum.add(p);
            pairCounter++;
        }
        return sum;
    }

    BigInteger array_a_difference_of_pair_products() {
        BigInteger sum = BigInteger.ZERO;
        int pairCounter = 0;

        for (int i = 0; i < a.size() - 1; i += 2) {
            BigInteger num1 = a.get(i);
            BigInteger num2 = a.get(i + 1);

            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairDifference = num1.subtract(num2);
            System.out.printf("A-%s %s %s %s %d %d\n", groupName, num1, num2, pairDifference, i, i + 1);

            BigInteger p = num1.multiply(num2);
            sum = sum.add(p);
            pairCounter++;
        }
        return sum;
    }

    BigInteger array_b_difference_of_pair_products() {
        BigInteger sum = BigInteger.ZERO;
        int pairCounter = 0;

        for (int i = 0; i < b.size() - 1; i += 2) {
            BigInteger num1 = b.get(i);
            BigInteger num2 = b.get(i + 1);

            String groupName = (pairCounter % 2 == 0) ? "ONE" : "TWO";
            BigInteger pairDifference = num1.subtract(num2);
            System.out.printf("B-%s %s %s %s %d %d\n", groupName, num1, num2, pairDifference, i, i + 1);

            BigInteger p = num1.multiply(num2);
            sum = sum.add(p);
            pairCounter++;
        }
        return sum;
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

class Main{
    static ArrayList<BigInteger> generateRandomArray() {
        Random rand = new Random();
        ArrayList<BigInteger> result = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            result.add(BigInteger.valueOf(rand.nextInt(101)));
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=====================HAMUEV OLEG S-T-A-R-T=====================" );
        System.out.println("Variant 9 - Hamuev Oleg - START");
        ArrayList<BigInteger> arrayA = generateRandomArray();
        ArrayList<BigInteger> arrayB = generateRandomArray();

        NumberMath math = new NumberMath(arrayA, arrayB);

        final BigInteger[] results = new BigInteger[2];

        Thread threadA = new Thread(() -> {
            results[0] = math.array_a_sum_of_pair_products();
            System.out.println("Thread for Array A finished calculation.");
        });

        Thread threadB = new Thread(() -> {
            results[1] = math.array_b_sum_of_pair_products();
            System.out.println("Thread for Array B finished calculation.");
        });

        System.out.println("Array A: " + arrayA);
        System.out.println("Array B: " + arrayB);

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        BigInteger sumA = results[0];
        BigInteger sumB = results[1];
        BigInteger difference = math.difference_of_array_a_and_b();
        BigInteger sum        = math.sum_of_array_a_and_b();

        System.out.println("Sum of pair products in A: " + sumA);
        System.out.println("Sum of pair products in B: " + sumB);
        System.out.println("Difference(PART-1): " + difference);
        System.out.println("Sum(PART-2): " + sum);
        System.out.println("Variant 9 - Hamuev Oleg - END");
        System.out.println("=====================HAMUEV OLEG E-N-D=====================" );
    }
}