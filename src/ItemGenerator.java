/**
 * An interface for any class that can generate an item.
 * This allows the Stocker (Producer) to not care *how* the item is generated.
 */
public interface ItemGenerator {
    /**
     * Generates a single item.
     * @return The generated item (an integer).
     */
    int generate();
}