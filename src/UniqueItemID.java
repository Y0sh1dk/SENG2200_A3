public class UniqueItemID {
    private static final UniqueItemID instance = new UniqueItemID();

    private static int nextUniqueID = 0;

    private UniqueItemID() {

    }

    public static String getID() {
        return String.valueOf(++nextUniqueID);
    }
}
