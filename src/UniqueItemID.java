/**
 * FileName: UniqueItemID.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Singleton class for creating unique item ID's
 */

public class UniqueItemID {
    private static final UniqueItemID instance = new UniqueItemID();    // Instance of itself
    private static int nextUniqueID = 0;                                // Counter for unique ID's

    /**
     * Constructor when no args are given
     * private because class should NOT be able to be instantiated more than once
     */
    private UniqueItemID() {

    }

    /**
     * getID() method
     * @return A unique ID in the form of a string
     */
    public static String getID() {
        return String.valueOf(++nextUniqueID);
    }
}
