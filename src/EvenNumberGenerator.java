import java.util.Random;

public class EvenNumberGenerator implements ItemGenerator {

    private final Random random = new Random();
    private final int maxBound; // The upper limit (e.g., 100)

    /**
     * Creates a generator for even numbers.
     * @param maxNumber The maximum number to generate (e.g., 100).
     * The generator will produce even numbers between 0 and maxNumber.
     */
    public EvenNumberGenerator(int maxNumber) {
        this.maxBound = (maxNumber / 2) + 1;
    }

    @Override
    public int generate() {
        // Generates a number from 0 to (maxBound-1) and multiplies by 2
        return random.nextInt(maxBound) * 2;
    }
}