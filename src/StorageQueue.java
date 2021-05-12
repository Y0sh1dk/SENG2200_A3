import java.util.LinkedList;
import java.util.Queue;

public class StorageQueue<T extends Item> {
    private Queue<T> itemsStored;
    private String ID;


    public StorageQueue() {
        this.ID = "Not assigned";
        this.itemsStored = new LinkedList<>(); // infinite size
    }


    public StorageQueue(String inID, int inMaxSize) {
        this();
        this.ID = inID;
        this.itemsStored = new LimitedLinkedList<>(inMaxSize); // Limited size
    }


    public boolean add(T inItem) {
        //System.out.println(inItem);
        return this.itemsStored.add(inItem);
    }

    public T remove() {
        return this.itemsStored.remove();
    }

    public int size() {
        return this.itemsStored.size();
    }


    // Lmao, lachlan will hate this
    // TODO(yoshi): test yo
    private class LimitedLinkedList<T> extends LinkedList<T> {
        private final int limit;

        public LimitedLinkedList(int inLimit) {
            this.limit = inLimit;
        }

        @Override
        public boolean add(T o) {
            if (this.size() == limit) {
                return false;
            }
            super.add(o);
            return true;
        }

    }
}
